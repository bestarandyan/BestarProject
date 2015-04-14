package com.manyi.mall.common.util;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

/**
 * Created by Administrator on 2015/4/8.
 */
public class QRCodeUtil {

    // 生成QR图
    private void createImage(String text) {
        try {
            //Encode with a QR Code image
            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(text, null, QRContents.Type.TEXT,
                    BarcodeFormat.QR_CODE.toString(),
                    200);

            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
