package com.android.funny.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {
	private static String sdState = Environment.getExternalStorageState();
	private static String path = Environment.getExternalStorageDirectory()
			.toString();
	public static final String PHTOT_NAME = "/PHOTO";

	public static final String PIC_NAME = "pic.jpg";

	private static final char last2byte = (char) Integer
			.parseInt("00000011", 2);
	private static final char last4byte = (char) Integer
			.parseInt("00001111", 2);
	private static final char last6byte = (char) Integer
			.parseInt("00111111", 2);
	private static final char lead6byte = (char) Integer
			.parseInt("11111100", 2);
	private static final char lead4byte = (char) Integer
			.parseInt("11110000", 2);
	private static final char lead2byte = (char) Integer
			.parseInt("11000000", 2);
	private static final char[] encodeTable = new char[] { 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/' };

	/**
	 * Base64 encoding.
	 *
	 * @param from
	 *            The src data.
	 * @return cryto_str
	 */
	public static String base64Encode(byte[] from) {
		StringBuilder to = new StringBuilder((int) (from.length * 1.34) + 3);
		int num = 0;
		char currentByte = 0;
		for (int i = 0; i < from.length; i++) {
			num = num % 8;
			while (num < 8) {
				switch (num) {
					case 0:
						currentByte = (char) (from[i] & lead6byte);
						currentByte = (char) (currentByte >>> 2);
						break;
					case 2:
						currentByte = (char) (from[i] & last6byte);
						break;
					case 4:
						currentByte = (char) (from[i] & last4byte);
						currentByte = (char) (currentByte << 2);
						if ((i + 1) < from.length) {
							currentByte |= (from[i + 1] & lead2byte) >>> 6;
						}
						break;
					case 6:
						currentByte = (char) (from[i] & last2byte);
						currentByte = (char) (currentByte << 4);
						if ((i + 1) < from.length) {
							currentByte |= (from[i + 1] & lead4byte) >>> 4;
						}
						break;
					default:
						break;
				}
				to.append(encodeTable[currentByte]);
				num += 6;
			}
		}
		if (to.length() % 4 != 0) {
			for (int i = 4 - to.length() % 4; i > 0; i--) {
				to.append("=");
			}
		}
		return to.toString();
	}

	/**
	 *
	 */
	public static byte[] bitmapToByte(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 把图片保存到sd卡中，保存的路径为storage/PHOTO;
	 *
	 * @param bitmap
	 * @param imageName
	 */
	public static void saveBitmap(Bitmap bitmap, String imageName) {
		File file;
		File PicName;
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			// 获得sd卡根目录
			file = new File(path + PHTOT_NAME);
			if (!file.exists()) {
				file.mkdirs();
			}
			PicName = new File(file, imageName);
			try {
				if (!PicName.exists()) {
					PicName.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(PicName);
				if (PicName.getName().endsWith(".png")) {
					bitmap.compress(CompressFormat.PNG, 70, fos);
				} else if (PicName.getName().endsWith(".jpg")) {
					bitmap.compress(CompressFormat.JPEG, 70, fos);
				}
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Uri getImageUri() {
		return Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + PHTOT_NAME, PIC_NAME));
	}

	/**
	 * 从sd卡中取出图片；
	 *
	 * @param imageName
	 * @return
	 */
	public static Bitmap getBitmap(String imageName) {
		Bitmap bitmap = null;
		File imagePic;
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {

			imagePic = new File(path + PHTOT_NAME, imageName);
			if (imagePic.exists()) {
				try {
					bitmap = BitmapFactory.decodeStream(new FileInputStream(
							imagePic));
				} catch (FileNotFoundException e) {
					// e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	/*保存相机图片到指定文件*/
	public static void saveImageFromCamera(String path, File file) {
		//先对图片进行质量压缩，并保存到本地
		Bitmap bitmap = BitmapFactory.decodeFile(path);
        /*保存图片*/
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 把压缩后的数据存放到baos中，压缩一半
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*展示图片时，需要改变图片的宽高大小*/
	public static Bitmap compressImage(String path, int viewWidth, int viewHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		//原始图片大小
		int outWidth = options.outWidth;
		int outHeight = options.outHeight;
		//获取控件的宽高
		int finalScale = 1;
		if (outWidth > viewWidth || outHeight > viewHeight) {
			int w = Math.round((float) outWidth / (float) viewWidth);
			int h = Math.round((float) outHeight / (float) viewHeight);
			if (w > h) {
				finalScale = w;
			} else {
				finalScale = h;
			}
		}
		options.inSampleSize = finalScale;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inJustDecodeBounds = false;
		Bitmap finalBitmap = BitmapFactory.decodeFile(path, options);
		return finalBitmap;
	}

	//保存相册图片
	public static void saveImageFromGallery(Context context, Uri uri, File file) {
//        this.mFile = file;
        /*先对图片进行质量压缩*/
		//根据路径获取到原图
		String realFilePath = getRealFilePath(context, uri);
		Bitmap bitmap = BitmapFactory.decodeFile(realFilePath);
        /*保存图片*/
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 把压缩后的数据存放到baos中，压缩一半
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*uri转换成文件路径*/
	private static String getRealFilePath(Context context, final Uri uri) {
		if (null == uri) {
			return null;
		}
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * 删除sd卡上的图片；
	 *
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				}
				// 如果它是一个目录
				else if (file.isDirectory()) {
					// 声明目录下所有的文件 files[];
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
						deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
					}
				}
				file.delete();
			}
		}
	}

	/**
	 * 把文本保存到SDCard/Android/data/你的应用的包名/files/ 目录下，当卸载应用的时候删除；
	 *
	 * @param context
	 * @param fileName
	 * @param text
	 */
	public static void savetoSDText(Context context, String fileName,
									String text) {
		// TODO Auto-generated method stub
		File file = context.getExternalFilesDir("/photo");
		try {
			FileOutputStream fos = new FileOutputStream(file + "/" + fileName);
			fos.write(text.getBytes());
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 把图片保存SDCard/Android/data/你的应用的包名/files/ 目录下 当卸载应用的时候删除
	 *
	 * @param context
	 * @param fileName
	 * @param bitmap
	 */
	public static void saveToSDBitmap(Context context, String fileName,
									  Bitmap bitmap) {
		File file = context.getExternalFilesDir("/photo");
		try {
			FileOutputStream fos = new FileOutputStream(file + "/" + fileName);
			bitmap.compress(CompressFormat.PNG, 75, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 得到圆角图片； bitmap:传入图片 roundPx :传入显示的圆角弧度，一般设置10.0就可以
	 */
	private static Bitmap output;

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		if (bitmap != null) {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		}
		return output;
	}

	/**
	 * 得到圆形图片；
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width,
				height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 判断sd卡是否可用；
	 *
	 * @return
	 */
	public static boolean isSdcardExisting() {
		final String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}

