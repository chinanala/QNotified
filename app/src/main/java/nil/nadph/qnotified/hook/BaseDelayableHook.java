/* QNotified - An Xposed module for QQ/TIM
 * Copyright (C) 2019-2020 xenonhydride@gmail.com
 * https://github.com/cinit/QNotified
 *
 * This software is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see
 * <https://www.gnu.org/licenses/>.
 */
package nil.nadph.qnotified.hook;

import me.kyuubiran.hook.AutomaticMosaicName;
import me.kyuubiran.hook.DisableScreenshotHelper;
import me.kyuubiran.hook.RemoveCameraButton;
import me.kyuubiran.hook.RemoveGroupApp;
import me.kyuubiran.hook.RemovePlayTogether;
import me.kyuubiran.hook.RemoveQbossAD;
import me.kyuubiran.hook.ShowSelfMsgByLeft;
import me.kyuubiran.hook.SimplifyQQSettingMe;
import me.singleneuron.hook.ChangeDrawerWidth;
import me.singleneuron.hook.DebugDump;
import me.singleneuron.hook.ForceSystemAlbum;
import me.singleneuron.hook.ForceSystemCamera;
import me.singleneuron.hook.ForceSystemFile;
import me.singleneuron.hook.NewRoundHead;
import me.singleneuron.hook.NoApplet;
import nil.nadph.qnotified.SyncUtils;
import nil.nadph.qnotified.config.SwitchConfigItem;
import nil.nadph.qnotified.hook.rikka.BaseApk;
import nil.nadph.qnotified.hook.rikka.CustomDeviceModel;
import nil.nadph.qnotified.hook.rikka.CustomMsgTimeFormat;
import nil.nadph.qnotified.hook.rikka.CustomSplash;
import nil.nadph.qnotified.hook.rikka.DefaultFont;
import nil.nadph.qnotified.hook.rikka.DisableAvatarDecoration;
import nil.nadph.qnotified.hook.rikka.DisableColorNickName;
import nil.nadph.qnotified.hook.rikka.DisableDropSticker;
import nil.nadph.qnotified.hook.rikka.DisableEnterEffect;
import nil.nadph.qnotified.hook.rikka.DisablePokeEffect;
import nil.nadph.qnotified.hook.rikka.IgnoreDiyCard;
import nil.nadph.qnotified.hook.rikka.OneTapTwentyLikes;
import nil.nadph.qnotified.hook.rikka.RemoveMiniProgramAd;
import nil.nadph.qnotified.hook.rikka.RemoveSendGiftAd;
import nil.nadph.qnotified.hook.rikka.ShowMsgCount;
import nil.nadph.qnotified.step.Step;
import nil.nadph.qnotified.util.NonNull;
import nil.nadph.qnotified.util.Utils;

public abstract class BaseDelayableHook implements SwitchConfigItem {

    private static BaseDelayableHook[] sAllHooks;

    private int myId = -1;

    public static BaseDelayableHook getHookByType(int hookId) {
        return queryDelayableHooks()[hookId];
    }

    public static BaseDelayableHook[] queryDelayableHooks() {
        if (sAllHooks == null) sAllHooks = new BaseDelayableHook[]{
                SettingEntryHook.get(),
                DelDetectorHook.get(),
                PttForwardHook.get(),
                MuteAtAllAndRedPacket.get(),
                CardMsgHook.get(),
                ChatTailHook.get(),
                FlashPicHook.get(),
                RepeaterHook.get(),
                EmoPicHook.get(),
                //GalleryBgHook.get(),
                FavMoreEmo.get(),
                RevokeMsgHook.get(),
                FakeVipHook.get(),
                HideGiftAnim.get(),
                SimpleCheckInHook.get(),
                PreUpgradeHook.get(),
                CheatHook.get(),
                RoundAvatarHook.get(),
                $endGiftHook.get(),
                MultiForwardAvatarHook.get(),
                ReplyNoAtHook.get(),
                MuteQZoneThumbsUp.get(),
                FakeBatteryHook.get(),
                FileRecvRedirect.get(),
                ShowPicGagHook.get(),
                DefaultBubbleHook.get(),
                DarkOverlayHook.get(),
                GagInfoDisclosure.get(),
                PicMd5Hook.get(),
                ShowMsgCount.get(),
                IgnoreDiyCard.get(),
                InspectMessage.get(),
                DefaultFont.get(),
                BaseApk.get(),
                DisableScreenshotHelper.get(),
                OneTapTwentyLikes.get(),
                DisableEnterEffect.get(),
                DisableColorNickName.get(),
                CustomMsgTimeFormat.get(),
                RemoveSendGiftAd.get(),
                DisableDropSticker.get(),
//                DisableShakeWindow.get(),
                DisablePokeEffect.get(),
                RemoveMiniProgramAd.get(),
                JumpController.get(),
                CustomDeviceModel.get(),
                CustomSplash.get(),
                DisableAvatarDecoration.get(),
                RemoveCameraButton.get(),
                RemovePlayTogether.get(),
                RemoveQbossAD.get(),
                NoApplet.INSTANCE,
                NewRoundHead.INSTANCE,
                ForceSystemCamera.INSTANCE,
                AutomaticMosaicName.INSTANCE,
                ForceSystemAlbum.INSTANCE,
                ForceSystemFile.INSTANCE,
                ShowSelfMsgByLeft.INSTANCE,
                RemoveGroupApp.INSTANCE,
                ScriptEventHook.get(),
                InputButtonHook.get(),
                SimplifyQQSettingMe.INSTANCE,
                DebugDump.INSTANCE,
                ChangeDrawerWidth.INSTANCE,
        };
        return sAllHooks;
    }

    public static void allowEarlyInit(BaseDelayableHook hook) {
        if (hook == null) return;
        try {
            if (hook.isTargetProc() && hook.isEnabled() && hook.checkPreconditions() && !hook.isInited())
                hook.init();
        } catch (Throwable e) {
            Utils.log(e);
        }
    }

    public boolean isTargetProc() {
        return (getEffectiveProc() & SyncUtils.getProcessType()) != 0;
    }

    public abstract int getEffectiveProc();

    public abstract boolean isInited();

    public abstract boolean init();

    @Override
    public boolean sync() {
        return true;
    }

    @NonNull
    public abstract Step[] getPreconditions();

    @Override
    public boolean isValid() {
        return true;
    }

    public boolean checkPreconditions() {
        for (Step i : getPreconditions()) {
            if (!i.isDone()) return false;
        }
        return true;
    }

    public int getId() {
        if (myId != -1) return myId;
        BaseDelayableHook[] hooks = queryDelayableHooks();
        for (int i = 0; i < hooks.length; i++) {
            if (hooks[i].getClass().equals(this.getClass())) {
                myId = i;
                return myId;
            }
        }
        return -1;
    }

    /**
     * safe to call, no Throwable allowed
     *
     * @return whether the config item is enabled
     */
    public abstract boolean isEnabled();

    /**
     * This method must be safe to call even if it is NOT inited
     *
     * @param enabled has no effect if isValid() returns false
     */
    @Override
    public abstract void setEnabled(boolean enabled);

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + (isInited() ? "inited" : "") + "," + (isEnabled() ? "enabled" : "") + "," + SyncUtils.getProcessName() + ")";
    }

}
