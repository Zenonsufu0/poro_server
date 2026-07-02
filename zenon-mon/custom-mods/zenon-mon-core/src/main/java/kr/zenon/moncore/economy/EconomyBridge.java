package kr.zenon.moncore.economy;

import kr.zenon.moncore.ZenonMonCore;
import kr.zenon.moncore.config.ConfigManager;
import kr.zenon.moncore.data.PlayerProgress;
import kr.zenon.moncore.data.ZenonMonState;
import kr.zenon.moncore.title.TitleService;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * 골드 잔액 API (economy_design.md §8, 단일 화폐 골드).
 * 저장은 PlayerProgress.balance. 모든 증감은 출처 태그와 함께 감사 로깅(§6 텔레메트리 1차).
 * 외부 경제 모드 대비 추상화 — 현재는 내부 PersistentState 직결.
 */
public final class EconomyBridge {
    private EconomyBridge() {}

    private static PlayerProgress progress(ServerPlayerEntity player) {
        return ZenonMonState.get(player.getServer()).getOrCreate(player.getUuid());
    }

    public static long getBalance(ServerPlayerEntity player) {
        return progress(player).balance;
    }

    /** 입금(보상·매입). 음수/0은 무시. */
    public static void deposit(ServerPlayerEntity player, long amount, String source) {
        deposit(player, amount, source, null, 0);
    }

    /** 입금(보상·매입). 품목/수량은 운영 통계용 선택 메타데이터. */
    public static void deposit(ServerPlayerEntity player, long amount, String source, String itemId, int count) {
        if (amount <= 0) return;
        ZenonMonState state = ZenonMonState.get(player.getServer());
        PlayerProgress p = state.getOrCreate(player.getUuid());
        p.lastKnownName = player.getGameProfile().getName();
        p.balance += amount;
        state.recordEconomyFlow(player.getUuid(), p.lastKnownName, amount, p.balance, source, itemId, count);
        TitleService.updateWealth(player);
        audit(player, "+" + amount, p.balance, source);
    }

    /** 출금(구매·해금). 잔액 부족 시 false(차감 안 함). */
    public static boolean withdraw(ServerPlayerEntity player, long amount, String source) {
        return withdraw(player, amount, source, null, 0);
    }

    /** 출금(구매·해금). 품목/수량은 운영 통계용 선택 메타데이터. */
    public static boolean withdraw(ServerPlayerEntity player, long amount, String source, String itemId, int count) {
        if (amount <= 0) return true;
        ZenonMonState state = ZenonMonState.get(player.getServer());
        PlayerProgress p = state.getOrCreate(player.getUuid());
        if (p.balance < amount) return false;
        p.lastKnownName = player.getGameProfile().getName();
        p.balance -= amount;
        state.recordEconomyFlow(player.getUuid(), p.lastKnownName, -amount, p.balance, source, itemId, count);
        TitleService.updateWealth(player);
        audit(player, "-" + amount, p.balance, source);
        return true;
    }

    /** 관리자 강제 설정. */
    public static void set(ServerPlayerEntity player, long amount, String source) {
        ZenonMonState state = ZenonMonState.get(player.getServer());
        PlayerProgress p = state.getOrCreate(player.getUuid());
        p.lastKnownName = player.getGameProfile().getName();
        long before = p.balance;
        p.balance = Math.max(0, amount);
        state.recordEconomyAdjustment(player.getUuid(), p.lastKnownName, p.balance - before, p.balance, source);
        TitleService.updateWealth(player);
        audit(player, "=" + p.balance, p.balance, source);
    }

    private static void audit(ServerPlayerEntity player, String delta, long balance, String source) {
        if (!ConfigManager.core().logging.auditEnabled) return;
        ZenonMonCore.LOGGER.info("[Economy] {} {} (잔액 {}) src={}",
                player.getGameProfile().getName(), delta, balance, source);
    }
}
