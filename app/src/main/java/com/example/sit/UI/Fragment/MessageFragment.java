package com.example.sit.UI.Fragment;

import android.view.View;

import com.example.sit.UI.Activity.ChatActivity;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;

public class MessageFragment extends EaseConversationListFragment {

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        Object item = conversationListLayout.getItem(position).getInfo();
        if (item instanceof EMConversation) {
            ChatActivity.actionStart(mContext, ((EMConversation) item).conversationId(), EaseCommonUtils.getChatType((EMConversation) item));
        }
    }

    @Override
    public void notifyAllChange() {
        super.notifyAllChange();
    }
}
