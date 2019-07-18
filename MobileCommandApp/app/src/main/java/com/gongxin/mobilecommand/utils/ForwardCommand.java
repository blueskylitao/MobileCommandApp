//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gongxin.mobilecommand.utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatDialog;

import com.kook.R.string;
import com.kook.im.model.chatmessage.MessageFactory;
import com.kook.im.model.forword.ForwardItem;
import com.kook.im.model.forword.ForwardUtils;
import com.kook.im.ui.cacheView.DataType;
import com.kook.im.ui.chat.CheckSendMsg;
import com.kook.im.util.DialogHelper;
import com.kook.im.util.choose.command.BaseCommand;
import com.kook.im.util.choose.command.ChooseContext;
import com.kook.im.util.choose.command.ChooseResultLogic;
import com.kook.im.util.choose.command.ContactDataSource;
import com.kook.im.util.choose.datasource.ChooseFactory;
import com.kook.im.util.choose.datasource.DataSource;
import com.kook.im.util.choose.datasource.ChooseFactory.StartType;
import com.kook.im.util.receiverDailog.ReceiverDailog;
import com.kook.im.util.receiverDailog.ReceiverItem;
import com.kook.im.util.receiverDailog.ReceiverDailog.DataOption;
import com.kook.im.util.receiverDailog.ReceiverDailog.OnSendClickListener;
import com.kook.sdk.api.EConvType;
import com.kook.sdk.wrapper.KKClient;
import com.kook.sdk.wrapper.msg.MsgService;
import com.kook.sdk.wrapper.msg.model.ForwardInfo;
import com.kook.sdk.wrapper.msg.model.IMMessage;
import com.kook.sdk.wrapper.msg.model.KKIMMessageFactory;
import com.kook.sdk.wrapper.msg.model.element.KKLinkCardElement;
import com.kook.view.dialog.DialogFactory;
import com.kook.webbase.link.LinkPaserManager;
import com.kook.webbase.link.LinkResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ForwardCommand extends BaseCommand {
    private List<ForwardItem> forwordItems;
    private AppCompatDialog loadingDialog;
    public ContactDataSource source;
    private String forwardText;
    private boolean moveTaskToBack;

    public ForwardCommand() {
        this.addDataSource(StartType.BOOT, (long) (ChooseFactory.CORP_TREE | ChooseFactory.GROUP_CONVERSATION | ChooseFactory.CONTACT | ChooseFactory.SELF_DEPT));
    }

    @Override
    public DataSource getDataSourceList() {
        this.source = new ContactDataSource();
        this.source.setOnlyUser(false);
        this.source.setHiddenExtContact(false);
        return this.source;
    }

    public void setMoveTaskToBack(boolean moveTaskToBack) {
        this.moveTaskToBack = moveTaskToBack;
    }

    public void setForwardItem(ForwardItem forwordItem) {
        this.forwordItems = new ArrayList();
        this.forwordItems.add(forwordItem);
    }

    public void setForwardItems(List<ForwardItem> forwordItems) {
        this.forwordItems = forwordItems;
    }

    public void setForwardText(String forwardText) {
        this.forwardText = forwardText;
    }

    @Override
    protected void onChooseDataLogic(final ChooseContext context, ChooseResultLogic logic) {
        if (CheckSendMsg.checkSendMsgWithDialog(context.getContext())) {
            if (TextUtils.isEmpty(this.forwardText)) {
                this.forwardText = context.getContext().getString(string.chat_msg_already_forward);
            }

            String desc = null;
            if (this.forwordItems.size() > 1) {
                desc = context.getContext().getResources().getString(string.chat_msg_multi_count);
                desc = String.format(desc, this.forwordItems.size());
            } else {
                desc = MessageFactory.getMessage(((ForwardItem) this.forwordItems.get(0)).getNewMsg()).getSummary(context.getContext());
            }

            ReceiverDailog dailog = logic.createReceiverDailog(context.getContext(), logic.getReceivers(), desc);
            dailog.setSendListener(new OnSendClickListener() {
                @SuppressLint({"StaticFieldLeak"})
                @Override
                public void onClick(DataOption option) {
                    final List<ReceiverItem> items = option.getReceiverItemList();

                    //拿到第一个转发的item 插入数据库

                    final String desc = option.getContent();
                    ForwardCommand.this.loadingDialog = DialogFactory.buildLoadingDialog(context.getContext(), context.getContext().getResources().getString(string.picker_processing), true, false);
                    ForwardCommand.this.loadingDialog.show();
                    (new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            List<ForwardInfo> forwordmsgs = new ArrayList();
                            List<IMMessage> justTextForWard = new ArrayList();
                            ForwardCommand.this.paserLinkCardIfNeed(ForwardCommand.this.forwordItems);
                            ForwardInfo forwordmsg = null;
                            Iterator var5 = items.iterator();

                            while (var5.hasNext()) {
                                ReceiverItem item = (ReceiverItem) var5.next();

                                Iterator var7 = ForwardCommand.this.forwordItems.iterator();

                                while (true) {
                                    while (var7.hasNext()) {
                                        ForwardItem forwordItem = (ForwardItem) var7.next();
                                        if (forwordItem.hasAttachment() && !forwordItem.isSendNewMsg()) {
                                            if (item.getDataType() == DataType.user) {
                                                forwordmsg = ForwardUtils.buildNewForwardInfo(forwordItem, EConvType.ECONV_TYPE_SINGLE, item.getTargetId());
                                            } else if (item.getDataType() == DataType.group) {
                                                forwordmsg = ForwardUtils.buildNewForwardInfo(forwordItem, EConvType.ECONV_TYPE_GROUP, item.getTargetId());
                                            }

                                            if (forwordmsg != null) {
                                                forwordmsgs.add(forwordmsg);
                                            }

                                            forwordmsg = null;
                                        } else {
                                            IMMessage newMsg = null;
                                            if (item.getDataType() == DataType.user) {
                                                newMsg = ForwardUtils.buildNewMsg(forwordItem, EConvType.ECONV_TYPE_SINGLE, item.getTargetId());
                                            } else if (item.getDataType() == DataType.group) {
                                                newMsg = ForwardUtils.buildNewMsg(forwordItem, EConvType.ECONV_TYPE_GROUP, item.getTargetId());
                                            }

                                            if (newMsg != null) {
                                                justTextForWard.add(newMsg);
                                            }

                                            forwordmsg = null;
                                        }
                                    }

                                    if (!TextUtils.isEmpty(desc)) {
                                        IMMessage descMsg = null;
                                        if (item.getDataType() == DataType.user) {
                                            descMsg = KKIMMessageFactory.createSendingTextMsg(EConvType.ECONV_TYPE_SINGLE, desc, item.getTargetId());
                                        } else if (item.getDataType() == DataType.group) {
                                            descMsg = KKIMMessageFactory.createSendingTextMsg(EConvType.ECONV_TYPE_GROUP, desc, item.getTargetId());
                                        }

                                        if (descMsg != null) {
                                            justTextForWard.add(descMsg);
                                        }
                                    }
                                    break;
                                }
                            }

                            ((MsgService) KKClient.getService(MsgService.class)).forwardMsg(forwordmsgs);
                            var5 = justTextForWard.iterator();
                            while (var5.hasNext()) {
                                IMMessage message = (IMMessage) var5.next();
                                ((MsgService) KKClient.getService(MsgService.class)).sendMessage(message);
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if (ForwardCommand.this.loadingDialog != null && ForwardCommand.this.loadingDialog.isShowing()) {
                                DialogHelper.dismiss(ForwardCommand.this.loadingDialog);
                                ForwardCommand.this.loadingDialog = null;
                            }

                            if (!context.isFinishing()) {
                                DialogFactory.showSimpleToast(context.getContext(), ForwardCommand.this.forwardText, true);
                                if (ForwardCommand.this.moveTaskToBack) {
                                    context.moveTaskToBack();
                                }

                                context.finish();
                            }

                        }
                    }).execute(new Void[0]);
                }
            });
            dailog.show();
        }
    }

    private void paserLinkCardIfNeed(List<ForwardItem> forwordItems) {
        List<String> paserLinks = new ArrayList();
        List<IMMessage> paserMsg = new ArrayList();
        Iterator var4 = forwordItems.iterator();

        while (var4.hasNext()) {
            ForwardItem forwordItem = (ForwardItem) var4.next();
            if (forwordItem.getNewMsg().getMsg().getmMsgType() != 8) {
                return;
            }

            KKLinkCardElement link = (KKLinkCardElement) forwordItem.getNewMsg().getFirstElement();
            paserMsg.add(forwordItem.getNewMsg());
            paserLinks.add(link.getUrl());
        }

        Map<String, LinkResult> resultMap = LinkPaserManager.paserUrlLinks(paserLinks);
        Iterator var10 = paserMsg.iterator();

        while (var10.hasNext()) {
            IMMessage imMessage = (IMMessage) var10.next();
            KKLinkCardElement link = (KKLinkCardElement) imMessage.getFirstElement();
            LinkResult result = (LinkResult) resultMap.get(link.getUrl());
            if (result != null) {
                if (!TextUtils.isEmpty(result.getPicUrl())) {
                    link.setIconUrl(result.getPicUrl());
                }

                if (!TextUtils.isEmpty(result.getContent())) {
                    link.setContent(result.getContent());
                }

                if (!TextUtils.isEmpty(result.getTitle())) {
                    link.setTitle(result.getTitle());
                }
            }
        }

    }
}
