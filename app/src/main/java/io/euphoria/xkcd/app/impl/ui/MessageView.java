package io.euphoria.xkcd.app.impl.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import io.euphoria.xkcd.app.R;
import io.euphoria.xkcd.app.data.SessionView;

import static io.euphoria.xkcd.app.impl.ui.UIUtils.hslToRgbInt;
import static io.euphoria.xkcd.app.impl.ui.UIUtils.nickColor;
import static io.euphoria.xkcd.app.impl.ui.UIUtils.setRoundedRectBackground;

public class MessageView extends BaseMessageView {

    private static final String TAG = "MessageView";

    private final MarginLayoutParams defaultLayoutParams;

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultLayoutParams = getDefaultMargins(context, attrs);
    }

    @Override
    protected void updateDisplay() {
        // TODO move to ViewHolder?
        TextView nickLbl = (TextView) findViewById(R.id.nick_lbl);
        TextView contentLbl = (TextView) findViewById(R.id.content_lbl);
        TextView collapseLbl = (TextView) findViewById(R.id.collapse_lbl);
        MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
        if (lp == null) {
            lp = new MarginLayoutParams(defaultLayoutParams);
            setLayoutParams(lp);
        }
        MessageTree message = getMessage();
        setMarginForIndent(getContext(), lp, message.getIndent());
        if (message.getMessage() != null) {
            contentLbl.setText(message.getMessage().getContent());
            SessionView sender = message.getMessage().getSender();
            nickLbl.setText(sender.getName());
            // Color the background of the sender's nick
            setRoundedRectBackground(nickLbl, nickColor(sender.getName()));
        } else {
            nickLbl.setText("N/A");
            contentLbl.setText("N/A");
            // Make nick background red
            setRoundedRectBackground(nickLbl, hslToRgbInt(0, 1, 0.5f));
            Log.e(TAG, "updateDisplay: MessageView message is null!",
                    new RuntimeException("MessageView message is null!"));
        }
        if (message.getReplies().isEmpty()) {
            collapseLbl.setText("");
            collapseLbl.setVisibility(GONE);
        } else {
            int replies = message.countVisibleUserReplies(true);
            if (replies == 0) {
                collapseLbl.setVisibility(GONE);
            } else {
                collapseLbl.setVisibility(VISIBLE);
                String pref = message.isCollapsed() ? "Show" : "Hide";
                String suff = replies == 1 ? "reply" : "replies";
                collapseLbl.setText(pref + " " + replies + " " + suff);
            }
        }
    }

    public void recycle() {
        super.recycle();
        setTextClickListener(null);
        setCollapserClickListener(null);
    }

    public void setTextClickListener(OnClickListener l) {
        UIUtils.setSelectableOnClickListener(findViewById(R.id.nick_lbl), l);
        UIUtils.setSelectableOnClickListener(findViewById(R.id.content_lbl), l);
    }
    public void setCollapserClickListener(OnClickListener l) {
        findViewById(R.id.collapse_lbl).setOnClickListener(l);
    }

}
