package com.courtesycarsredhill.util;

import android.text.TextUtils;
import android.widget.EditText;


import java.util.regex.Pattern;

/**
 * Created by viraj.patel on 11-Sep-18
 */
public class ValidationUtils {

    /**
     * Check EditText is Empty or not
     * Input:
     * 1. EditText
     * Output: True / False
     */
    public static boolean isFieldEmpty(EditText et) {
        return TextUtils.isEmpty(et.getText().toString().trim());
    }

    /**
     * Regex pattern for Email Validation
     */
    private static String regexEmail =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
    private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(regexEmail, Pattern.CASE_INSENSITIVE);

    /**
     * Check Email String is Valid or Not
     * Input:
     * 1. Email String
     * Output: True / False
     */
    public static boolean isValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(CharSequence target) {
        return target.length() >= 8 && target.length() <= 15 && android.util.Patterns.PHONE.matcher(target).matches();
    }

    /*public static boolean isValidQRCode(String QRCode) {
        if (QRCode == null || QRCode.length() == 0 || QRCode.equals("null")) {
            return false;
        }
        String plainText = "";
        try {
            plainText = CryptLib.getInstance().decryptCipherText(QRCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (plainText.length() == 0)
            return false;
        String codeIdentifier = plainText.substring(0, 1);
        return (plainText.contains("|") && (codeIdentifier.equals(CodeIdentifierEnum.USER.getValue()) || codeIdentifier.equals(CodeIdentifierEnum.WITHDRAW.getValue()) || codeIdentifier.equals(CodeIdentifierEnum.BILL.getValue()) || codeIdentifier.equals(CodeIdentifierEnum.ITEM_INVOICE.getValue())));
    }*/
}
