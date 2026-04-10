package com.poro.empire.command;

import com.poro.empire.classes.PlayerClass;
import com.poro.empire.combat.SkillService;
import com.poro.empire.reputation.ReputationManager;
import com.poro.empire.reputation.ReputationTier;
import com.poro.empire.storage.PlayerData;
import com.poro.empire.storage.PlayerDataManager;
import com.poro.empire.util.HealthHudFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EmpireCommand implements CommandExecutor, TabCompleter {
    private static final List<String> CLASS_NAMES = List.of("warrior", "assassin", "mage");
    private static final List<String> REPUTATION_ACTIONS = List.of("add", "remove", "set");
    private static final List<String> SUBCOMMANDS = List.of("class", "skill", "info", "setclass", "hud", "reputation");

    private final PlayerDataManager playerDataManager;
    private final SkillService skillService;
    private final ReputationManager reputationManager;

    public EmpireCommand(PlayerDataManager playerDataManager, SkillService skillService, ReputationManager reputationManager) {
        this.playerDataManager = playerDataManager;
        this.skillService = skillService;
        this.reputationManager = reputationManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase(Locale.ROOT);
        return switch (subCommand) {
            case "class" -> handleClassCommand(sender, args);
            case "skill" -> handleSkillCommand(sender, args);
            case "info" -> handleInfoCommand(sender);
            case "setclass" -> handleSetClassCommand(sender, args);
            case "hud" -> handleHudCommand(sender);
            case "reputation" -> handleReputationCommand(sender, args);
            default -> {
                sendUsage(sender);
                yield true;
            }
        };
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return filterByPrefix(SUBCOMMANDS, args[0]);
        }
        if (args.length == 2 && "class".equalsIgnoreCase(args[0])) {
            return filterByPrefix(CLASS_NAMES, args[1]);
        }
        if (args.length == 2 && "skill".equalsIgnoreCase(args[0])) {
            return filterByPrefix(skillService.getSkillKeys().stream().toList(), args[1]);
        }
        if (args.length == 2 && "setclass".equalsIgnoreCase(args[0])) {
            List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .toList();
            return filterByPrefix(onlinePlayers, args[1]);
        }
        if (args.length == 3 && "setclass".equalsIgnoreCase(args[0])) {
            return filterByPrefix(CLASS_NAMES, args[2]);
        }
        if (args.length == 2 && "reputation".equalsIgnoreCase(args[0])) {
            return filterByPrefix(REPUTATION_ACTIONS, args[1]);
        }
        if (args.length == 3 && "reputation".equalsIgnoreCase(args[0])) {
            if (REPUTATION_ACTIONS.contains(args[1].toLowerCase(Locale.ROOT))) {
                List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .toList();
                return filterByPrefix(onlinePlayers, args[2]);
            }
        }
        if (args.length == 4 && "reputation".equalsIgnoreCase(args[0])) {
            if (REPUTATION_ACTIONS.contains(args[1].toLowerCase(Locale.ROOT))) {
                return filterByPrefix(List.of("10", "50", "100", "500"), args[3]);
            }
        }
        return List.of();
    }

    private boolean handleClassCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use /empire class.");
            return true;
        }
        if (!player.hasPermission("empire.class")) {
            player.sendMessage(ChatColor.RED + "You do not have permission: empire.class");
            return true;
        }
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /empire class <warrior|assassin|mage>");
            return true;
        }
        return handleClassSelection(player, args[1]);
    }

    private boolean handleSkillCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use /empire skill.");
            return true;
        }
        if (!player.hasPermission("empire.skill")) {
            player.sendMessage(ChatColor.RED + "You do not have permission: empire.skill");
            return true;
        }
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /empire skill <slash|shadowstep|firebolt>");
            return true;
        }
        return skillService.useSkill(player, args[1]);
    }

    private boolean handleInfoCommand(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use /empire info.");
            return true;
        }
        if (!player.hasPermission("empire.info")) {
            player.sendMessage(ChatColor.RED + "You do not have permission: empire.info");
            return true;
        }

        PlayerData playerData = playerDataManager.getOrCreate(player.getUniqueId());
        String className = playerData.getPlayerClass().name().toLowerCase(Locale.ROOT);
        double health = player.getHealth();
        String weaponType = resolveWeaponType(player);

        player.sendMessage(ChatColor.GOLD + "=== Empire Debug Info ===");
        player.sendMessage(ChatColor.YELLOW + "Class: " + ChatColor.WHITE + className);
        player.sendMessage(ChatColor.YELLOW + "Health: " + ChatColor.WHITE + String.format(Locale.US, "%.1f", health));
        player.sendMessage(ChatColor.YELLOW + "Weapon Type: " + ChatColor.WHITE + weaponType);
        return true;
    }

    private boolean handleSetClassCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("empire.admin.setclass")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission: empire.admin.setclass");
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /empire setclass <player> <warrior|assassin|mage>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found or offline: " + args[1]);
            return true;
        }

        PlayerClass selectedClass = parseClass(args[2]);
        if (selectedClass == null) {
            sender.sendMessage(ChatColor.RED + "Invalid class. Choose one of: warrior, assassin, mage.");
            return true;
        }

        playerDataManager.setPlayerClass(target.getUniqueId(), selectedClass);
        sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s class to " + selectedClass.name().toLowerCase(Locale.ROOT) + ".");
        target.sendMessage(ChatColor.YELLOW + "Your class was set to " + selectedClass.name().toLowerCase(Locale.ROOT) + " by an administrator.");
        return true;
    }

    private boolean handleHudCommand(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use /empire hud.");
            return true;
        }
        if (!player.hasPermission("empire.hud")) {
            player.sendMessage(ChatColor.RED + "You do not have permission: empire.hud");
            return true;
        }

        String hudMessage = HealthHudFormatter.format(player);
        player.sendActionBar(Component.text(hudMessage));
        player.sendMessage(ChatColor.GREEN + "HUD test message sent: " + ChatColor.WHITE + hudMessage);
        return true;
    }

    private boolean handleReputationCommand(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return showOwnReputation(sender);
        }

        String action = args[1].toLowerCase(Locale.ROOT);
        if (!REPUTATION_ACTIONS.contains(action)) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /empire reputation");
            sender.sendMessage(ChatColor.YELLOW + "Usage: /empire reputation add <player> <amount>");
            sender.sendMessage(ChatColor.YELLOW + "Usage: /empire reputation remove <player> <amount>");
            sender.sendMessage(ChatColor.YELLOW + "Usage: /empire reputation set <player> <amount>");
            return true;
        }

        if (!sender.hasPermission("empire.admin.reputation")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission: empire.admin.reputation");
            return true;
        }
        if (args.length < 4) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /empire reputation " + action + " <player> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[2]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found or offline: " + args[2]);
            return true;
        }

        Integer amount = parseAmount(args[3]);
        if (amount == null) {
            sender.sendMessage(ChatColor.RED + "Amount must be a non-negative integer.");
            return true;
        }

        return switch (action) {
            case "add" -> handleReputationAdd(sender, target, amount);
            case "remove" -> handleReputationRemove(sender, target, amount);
            case "set" -> handleReputationSet(sender, target, amount);
            default -> true;
        };
    }

    private boolean showOwnReputation(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use /empire reputation.");
            return true;
        }
        if (!player.hasPermission("empire.reputation")) {
            player.sendMessage(ChatColor.RED + "You do not have permission: empire.reputation");
            return true;
        }

        int reputation = reputationManager.getReputation(player.getUniqueId());
        ReputationTier tier = reputationManager.getTier(player.getUniqueId());
        ReputationTier nextTier = getNextTier(tier);

        player.sendMessage(ChatColor.GOLD + "=== Empire Reputation ===");
        player.sendMessage(ChatColor.YELLOW + "명성치: " + ChatColor.WHITE + reputation);
        player.sendMessage(ChatColor.YELLOW + "단계: " + ChatColor.WHITE + tier.getDisplayName()
                + ChatColor.DARK_GRAY + " (" + tier.name() + ")");

        if (nextTier != null) {
            int needed = Math.max(0, nextTier.getMinimumReputation() - reputation);
            player.sendMessage(ChatColor.YELLOW + "다음 단계: " + ChatColor.WHITE + nextTier.getDisplayName()
                    + ChatColor.GRAY + " (+" + needed + ")");
        } else {
            player.sendMessage(ChatColor.YELLOW + "다음 단계: " + ChatColor.WHITE + "최고 단계");
        }
        return true;
    }

    private boolean handleReputationAdd(CommandSender sender, Player target, int amount) {
        reputationManager.addReputation(target.getUniqueId(), amount);
        int updated = reputationManager.getReputation(target.getUniqueId());
        ReputationTier tier = reputationManager.getTier(target.getUniqueId());

        sender.sendMessage(ChatColor.GREEN + "Added " + amount + " reputation to " + target.getName()
                + ". Current: " + updated + " (" + tier.getDisplayName() + ")");
        target.sendMessage(ChatColor.YELLOW + "명성치가 " + ChatColor.GREEN + "+" + amount
                + ChatColor.YELLOW + " 변경되었습니다. 현재: " + ChatColor.WHITE + updated
                + ChatColor.GRAY + " (" + tier.getDisplayName() + ")");
        return true;
    }

    private boolean handleReputationRemove(CommandSender sender, Player target, int amount) {
        reputationManager.removeReputation(target.getUniqueId(), amount);
        int updated = reputationManager.getReputation(target.getUniqueId());
        ReputationTier tier = reputationManager.getTier(target.getUniqueId());

        sender.sendMessage(ChatColor.GREEN + "Removed " + amount + " reputation from " + target.getName()
                + ". Current: " + updated + " (" + tier.getDisplayName() + ")");
        target.sendMessage(ChatColor.YELLOW + "명성치가 " + ChatColor.RED + "-" + amount
                + ChatColor.YELLOW + " 변경되었습니다. 현재: " + ChatColor.WHITE + updated
                + ChatColor.GRAY + " (" + tier.getDisplayName() + ")");
        return true;
    }

    private boolean handleReputationSet(CommandSender sender, Player target, int amount) {
        reputationManager.setReputation(target.getUniqueId(), amount);
        int updated = reputationManager.getReputation(target.getUniqueId());
        ReputationTier tier = reputationManager.getTier(target.getUniqueId());

        sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s reputation to "
                + updated + " (" + tier.getDisplayName() + ")");
        target.sendMessage(ChatColor.YELLOW + "명성치가 " + ChatColor.WHITE + updated
                + ChatColor.YELLOW + "(으)로 설정되었습니다."
                + ChatColor.GRAY + " (" + tier.getDisplayName() + ")");
        return true;
    }

    private boolean handleClassSelection(Player player, String classInput) {
        PlayerClass selectedClass = parseClass(classInput);
        if (selectedClass == null) {
            player.sendMessage(ChatColor.RED + "Invalid class. Choose one of: warrior, assassin, mage.");
            return true;
        }

        PlayerData playerData = playerDataManager.getOrCreate(player.getUniqueId());
        if (playerData.getPlayerClass().isSelected()) {
            player.sendMessage(ChatColor.RED + "You already selected a class: "
                    + playerData.getPlayerClass().name().toLowerCase(Locale.ROOT));
            return true;
        }

        playerDataManager.setPlayerClass(player.getUniqueId(), selectedClass);
        player.sendMessage(ChatColor.GREEN + "Class selected: " + selectedClass.name().toLowerCase(Locale.ROOT));
        return true;
    }

    private String resolveWeaponType(Player player) {
        Material material = player.getInventory().getItemInMainHand().getType();
        if (material == Material.AIR) {
            return "none";
        }

        String name = material.name();
        if ("CROSSBOW".equals(name)) {
            return "crossbow";
        }
        if ("TRIDENT".equals(name)) {
            return "trident";
        }
        if (name.endsWith("_SWORD")) {
            return "sword";
        }
        if (name.endsWith("_AXE")) {
            return "axe";
        }
        if (name.endsWith("BOW")) {
            return "bow";
        }
        if (name.endsWith("_HOE")) {
            return "hoe";
        }
        if (name.endsWith("_PICKAXE")) {
            return "pickaxe";
        }
        if (name.endsWith("_SHOVEL")) {
            return "shovel";
        }
        return name.toLowerCase(Locale.ROOT);
    }

    private @Nullable PlayerClass parseClass(String input) {
        return switch (input.toLowerCase(Locale.ROOT)) {
            case "warrior" -> PlayerClass.WARRIOR;
            case "assassin" -> PlayerClass.ASSASSIN;
            case "mage" -> PlayerClass.MAGE;
            default -> null;
        };
    }

    private @Nullable Integer parseAmount(String input) {
        try {
            int amount = Integer.parseInt(input);
            if (amount < 0) {
                return null;
            }
            return amount;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private @Nullable ReputationTier getNextTier(ReputationTier currentTier) {
        ReputationTier[] values = ReputationTier.values();
        int currentIndex = Arrays.asList(values).indexOf(currentTier);
        if (currentIndex < 0 || currentIndex + 1 >= values.length) {
            return null;
        }
        return values[currentIndex + 1];
    }

    private List<String> filterByPrefix(List<String> values, String prefix) {
        String loweredPrefix = prefix.toLowerCase(Locale.ROOT);
        return values.stream()
                .filter(value -> value.toLowerCase(Locale.ROOT).startsWith(loweredPrefix))
                .toList();
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Usage: /empire class <warrior|assassin|mage>");
        sender.sendMessage(ChatColor.YELLOW + "Usage: /empire skill <slash|shadowstep|firebolt>");
        sender.sendMessage(ChatColor.YELLOW + "Usage: /empire info");
        sender.sendMessage(ChatColor.YELLOW + "Usage: /empire hud");
        sender.sendMessage(ChatColor.YELLOW + "Usage: /empire setclass <player> <warrior|assassin|mage>");
        sender.sendMessage(ChatColor.YELLOW + "Usage: /empire reputation");
        sender.sendMessage(ChatColor.YELLOW + "Usage: /empire reputation <add|remove|set> <player> <amount>");
    }
}
