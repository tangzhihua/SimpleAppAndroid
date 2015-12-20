package core_lib.toolutils;

import android.support.annotation.ColorInt;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * 对 SpannableStringBuilder 的一个简易封装, 因为SpannableStringBuilder的使用要设置 start 和 end 索引, 这个很麻烦, 所以我改成 追加的方式, 就简单了.
 * Created by zhihuatang on 15/12/2.
 */
public final class SpannableStringHelperBuilder {
    private final SpannableStringBuilder spannableStringBuilder;

    public SpannableStringHelperBuilder() {
        spannableStringBuilder = new SpannableStringBuilder();
    }

    public SpannableStringHelperBuilder append(CharSequence text) {
        spannableStringBuilder.append(text);
        return this;
    }

    public SpannableStringHelperBuilder append(@ColorInt int textColor, CharSequence text) {
        return append(textColor, text, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public SpannableStringHelperBuilder append(@ColorInt int textColor, CharSequence text, int flags) {
        return append(new ForegroundColorSpan(textColor), text, flags);
    }

    public SpannableStringHelperBuilder append(Object what, CharSequence text, int flags) {
        final SpannableString spannableString = new SpannableString(text);
        // 注意 : int start(闭), int end(开)
        spannableString.setSpan(what, 0, text.length(), flags);
        spannableStringBuilder.append(spannableString);
        return this;
    }

    public SpannableStringBuilder build() {
        return spannableStringBuilder;
    }
}
