
package hd.produce.security.cn;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

import hd.source.crop.CropImageView;
import hd.source.crop.HighlightView;
import hd.utils.cn.Constants;
import hd.utils.cn.FileUtils;

public class CropActivity extends MonitoredActivity {

    // private static final String TAG = "CropImage";

    private static final boolean RECYCLE_INPUT = true;

    private int mAspectX, mAspectY;

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    break;
            }
        }
    };

    // These options specifiy the output image size and whether we should
    // scale the output to fit it (or just crop it).
    private int mOutputX, mOutputY;

    private boolean mScale;

    private boolean mScaleUp = true;

    private boolean mCircleCrop = false;

    public boolean mSaving; // Whether the "save" button is already clicked.

    private CropImageView mImageView;

    private Bitmap mBitmap;

    private Bitmap currentBitmap;

    // private RotateBitmap rotateBitmap;
    HighlightView mCrop;

    Uri targetUri;

    Uri saveUri;

    HighlightView hv;

    private int rotation = 0;

    private static final int ONE_K = 1024;

    private static final int ONE_M = ONE_K * ONE_K;

    private ContentResolver mContentResolver;

    private static final int DEFAULT_WIDTH = 512;

    private static final int DEFAULT_HEIGHT = 384;

    private int width;

    private int height;

    private int sampleSize = 1;

    // private BitmapManagers bitmapManager ;

    private static final int NORMAL = 1;

    private static final int REMINISCENCE = 2;

    private static final int SHARPEN = 3;

    private static final int RELIEF = 4;

    private static final int NEGATIVES = 5;

    private static final int ILLUMINATION = 6;

    private static final int HALO = 7;

    private static int bitmapClass = NORMAL;
    
    private Button back;
    private TextView title;
    private EditText remarkEdit;
    private TextView save;
    private TextView next;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Make UI fullscreen.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_crop);
        HdCnApp.getApplication().addActivity(this);
        // bitmapManager = BitmapManagers.getInstance();
        initViews();
        setListener();

        Intent intent = getIntent();
        targetUri = intent.getParcelableExtra(Constants.IMAGE_URI);
        mContentResolver = getContentResolver();

        boolean isBitmapRotate = false;
        if (mBitmap == null) {
            String path = getFilePath(targetUri);

            isBitmapRotate = isRotateImage(path);
            getBitmapSize();
            getBitmap();

            // 锟斤拷锟斤拷锟铰伙拷取锟斤拷Bitmap
            // saveBitmap(path);

        }

        if (currentBitmap == null) {
            finish();
            return;
        }

        startFaceDetection(isBitmapRotate, NORMAL);
    }

    private void saveBitmap(String imgPath) {

        final String savePath = imgPath.replace(".", "_save_for_filter.").trim();
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                saveDrawableToCache(mBitmap, savePath);
            }

        });

        saveUri = Uri.fromFile(new File(savePath));
    }

    /**
     * 锟剿达拷写锟斤拷锟斤拷锟斤拷锟斤拷
     *
     * @return void
     * @Title: initViews
     * @date 2012-12-14 锟斤拷锟斤拷10:41:23
     */
    private void initViews() {
        mImageView = (CropImageView) findViewById(R.id.image);
        mImageView.mContext = this;

        back = (Button)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title_text);
        title.setText(R.string.picture_crop);
        remarkEdit = (EditText)findViewById(R.id.remark_edit);
        save = (TextView)findViewById(R.id.save_text);
        next = (TextView)findViewById(R.id.next_text);
    }
    private void setListener(){
        back.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
        next.setOnClickListener(onClickListener);
    }
    private void onBackClick(){
        Intent intent = new Intent("inline-data");
        setResult(RESULT_CANCELED, intent);
        super.onBackPressed();
    }
    final OnClickListener onClickListener = new OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.back:
                    onBackClick();
                    break;
                case R.id.save_text:
                    onSaveClicked(false);
                    break;
                case R.id.next_text:
                    onSaveClicked(true);
                    break;

                default:
                    break;
            }
        }
        
    };
    /**
     * 锟斤拷取Bitmap锟街憋拷锟绞ｏ拷太锟斤拷锟剿就斤拷锟斤拷压锟斤拷
     *
     * @return void
     * @Title: getBitmapSize
     * @date 2012-12-14 锟斤拷锟斤拷8:32:13
     */
    private void getBitmapSize() {
        InputStream is = null;

        try {

            is = getInputStream(targetUri);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);

            width = options.outWidth;
            height = options.outHeight;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }

        }
    }

    /**
     * 锟剿达拷写锟斤拷锟斤拷锟斤拷锟斤拷
     *
     * @param intent
     * @return void
     * @Title: getBitmap
     * @date 2012-12-13 锟斤拷锟斤拷8:22:23
     */
    private void getBitmap() {
        InputStream is = null;
        try {

            try {
                is = getInputStream(targetUri);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            while ((width / sampleSize > DEFAULT_WIDTH * 2)
                    || (height / sampleSize > DEFAULT_HEIGHT * 2)) {
                sampleSize *= 2;
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;

            mBitmap = BitmapFactory.decodeStream(is, null, options);

            currentBitmap = mBitmap;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private Bitmap getBitmap(Uri uri) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {

            try {
                is = getInputStream(uri);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            bitmap = BitmapFactory.decodeStream(is);

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }

        return bitmap;
    }

    /**
     * 锟剿达拷写锟斤拷锟斤拷锟斤拷锟斤拷
     *
     * @param path
     * @return void
     * @Title: rotateImage
     * @date 2012-12-14 锟斤拷锟斤拷10:58:26
     */
    private boolean isRotateImage(String path) {

        try {
            ExifInterface exifInterface = new ExifInterface(path);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 锟斤拷取锟斤拷锟斤拷锟斤拷
     *
     * @param mUri
     * @return InputStream
     * @Title: getInputStream
     * @date 2012-12-14 锟斤拷锟斤拷9:00:31
     */
    private InputStream getInputStream(Uri mUri) throws IOException {
        try {
            if (mUri.getScheme().equals("file")) {
                return new java.io.FileInputStream(mUri.getPath());
            } else {
                return mContentResolver.openInputStream(mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * 锟斤拷锟経ri锟斤拷锟斤拷锟侥硷拷路锟斤拷
     *
     * @param mUri
     * @return String
     * @Title: getInputString
     * @date 2012-12-14 锟斤拷锟斤拷9:14:19
     */
    private String getFilePath(Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return mUri.getPath();
            } else {
                return getFilePathByUri(mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * 锟剿达拷写锟斤拷锟斤拷锟斤拷锟斤拷
     *
     * @param mUri
     * @return String
     * @Title: getFilePathByUri
     * @date 2012-12-14 锟斤拷锟斤拷9:16:33
     */
    private String getFilePathByUri(Uri mUri) throws FileNotFoundException {
        String imgPath;
        Cursor cursor = mContentResolver.query(mUri, null, null, null, null);
        cursor.moveToFirst();
        imgPath = cursor.getString(1); // 图片锟侥硷拷路锟斤拷
        return imgPath;
    }

    /**
     * 锟剿达拷写锟斤拷锟斤拷锟斤拷锟斤拷
     *
     * @param isRotate 锟角凤拷锟斤拷转图片
     * @return void
     * @Title: startFaceDetection
     * @date 2012-12-14 锟斤拷锟斤拷10:38:29
     */
    private void startFaceDetection(final boolean isRotate, final int handleClass) {
        if (isFinishing()) {
            return;
        }

        // if(hv != null){
        // mImageView.remove(hv);
        // }

        startBackgroundJob(this, null, getResources().getString(R.string.runningFaceDetection),
                new Runnable() {
                    public void run() {

                        final CountDownLatch latch = new CountDownLatch(1);

                        mHandler.post(new Runnable() {
                            @SuppressLint("NewApi")
                            public void run() {

                                if (hv != null) {
                                    mImageView.remove(hv);
                                }

                                if (isRotate) {
                                    initBitmap();
                                }
                                int apiLevel = Build.VERSION.SDK_INT;

                                if (apiLevel >= 11) {
                                    mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                                }

                                mImageView.setImageBitmapResetBase(currentBitmap, true);

                                final Bitmap b = currentBitmap;
                                if (b != currentBitmap && b != null) {

                                    mImageView.setImageBitmapResetBase(b, true);
                                    currentBitmap.recycle();
                                    currentBitmap = b;
                                }
                                if (mImageView.getScale() == 1F) {
                                    mImageView.center(true, true);
                                }
                                latch.countDown();
                            }
                        });
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        mRunFaceDetection.run();
                    }
                }, mHandler);
    }

    /**
     * 锟斤拷转原图
     *
     * @return void
     * @Title: initBitmap
     * @date 2012-12-13 锟斤拷锟斤拷5:37:15
     */
    private void initBitmap() {

        Matrix m = new Matrix();
        m.setRotate(90);

        int width = currentBitmap.getWidth();
        int height = currentBitmap.getHeight();

        try {
            currentBitmap = Bitmap.createBitmap(currentBitmap, 0, 0, width, height, m, true);
        } catch (OutOfMemoryError ooe) {

            m.postScale((float) 1 / sampleSize, (float) 1 / sampleSize);
            currentBitmap = Bitmap.createBitmap(currentBitmap, 0, 0, width, height, m, true);

        }

    }

    private static class BackgroundJob extends MonitoredActivity.LifeCycleAdapter implements
            Runnable {

        private final MonitoredActivity mActivity;

        private final ProgressDialog mDialog;

        private final Runnable mJob;

        private final Handler mHandler;

        private final Runnable mCleanupRunner = new Runnable() {
            public void run() {
                mActivity.removeLifeCycleListener(BackgroundJob.this);
                if (mDialog.getWindow() != null)
                    mDialog.dismiss();
            }
        };

        public BackgroundJob(MonitoredActivity activity, Runnable job, ProgressDialog dialog,
                             Handler handler) {
            mActivity = activity;
            mDialog = dialog;
            mJob = job;
            mActivity.addLifeCycleListener(this);
            mHandler = handler;
        }

        public void run() {
            try {
                mJob.run();
            } finally {
                mHandler.post(mCleanupRunner);
            }
        }

        @Override
        public void onActivityDestroyed(MonitoredActivity activity) {
            // We get here only when the onDestroyed being called before
            // the mCleanupRunner. So, run it now and remove it from the queue
            mCleanupRunner.run();
            mHandler.removeCallbacks(mCleanupRunner);
        }

        @Override
        public void onActivityStopped(MonitoredActivity activity) {
            mDialog.hide();
        }

        @Override
        public void onActivityStarted(MonitoredActivity activity) {
            mDialog.show();
        }
    }

    private static void startBackgroundJob(MonitoredActivity activity, String title,
                                           String message, Runnable job, Handler handler) {
        // Make the progress dialog uncancelable, so that we can gurantee
        // the thread will be done before the activity getting destroyed.
        ProgressDialog dialog = ProgressDialog.show(activity, title, message, true, false);
        new Thread(new BackgroundJob(activity, job, dialog, handler)).start();
    }

    Runnable mRunFaceDetection = new Runnable() {
        float mScale = 1F;

        Matrix mImageMatrix;

        // Create a default HightlightView if we found no face in the picture.
        private void makeDefault() {
            // mImageView.re

            hv = new HighlightView(mImageView);

            int width = currentBitmap.getWidth();
            int height = currentBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            if (mAspectX != 0 && mAspectY != 0) {
                if (mAspectX > mAspectY) {
                    cropHeight = cropWidth * mAspectY / mAspectX;
                } else {
                    cropWidth = cropHeight * mAspectX / mAspectY;
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop, mAspectX != 0 && mAspectY != 0);
            mImageView.add(hv);
        }

        public void run() {
            mImageMatrix = mImageView.getImageMatrix();

            mScale = 1.0F / mScale;
            mHandler.post(new Runnable() {
                public void run() {
                    makeDefault();

                    mImageView.invalidate();
                    if (mImageView.mHighlightViews.size() == 1) {
                        mCrop = mImageView.mHighlightViews.get(0);
                        mCrop.setFocus(true);
                    }
                }
            });
        }
    };

    /**
     * 锟斤拷转图片锟斤拷每锟斤拷锟斤拷90锟斤拷为锟斤拷位
     *
     * @return void
     * @Title: onRotateClicked
     * @date 2012-12-12 锟斤拷锟斤拷5:19:21
     */
    private void onRotateClicked() {

        startFaceDetection(true, bitmapClass);

    }

    /**
     * 锟斤拷锟斤拷锟斤拷锟侥达拷锟�
     * 锟斤拷锟斤保锟斤拷晒锟斤拷卮锟斤拷锟斤拷锟揭伙拷锟経ri锟斤拷系统默锟较达拷锟截碉拷锟斤拷一锟斤拷bitmap图锟斤拷
     * 锟斤拷锟截碉拷bitmap图锟饺较达拷幕锟斤拷突锟斤拷锟斤拷锟较低筹拷锟斤拷?锟结报锟斤拷锟斤拷一锟斤拷锟届常锟斤拷
     * android.os.transactiontoolargeexception锟斤拷为锟剿癸拷锟斤拷锟斤拷锟届常锟斤拷
     * 锟斤拷取锟剿达拷锟斤拷Uri锟侥凤拷锟斤拷锟斤拷
     *
     * @return void
     * @Title: onSaveClicked
     * @date 2012-12-14 锟斤拷锟斤拷10:32:38
     */
    private void onSaveClicked(boolean next) {
        // TODO this code needs to change to use the decode/crop/encode single
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mCrop == null) {
            return;
        }

        if (mSaving)
            return;
        mSaving = true;

        final Bitmap croppedImage;

        Rect r = mCrop.getCropRect();

        int width = r.width();
        int height = r.height();

        // If we are circle cropping, we want alpha channel, which is the
        // third param here.

        croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(croppedImage);
        Rect dstRect = new Rect(0, 0, width, height);

        canvas.drawBitmap(currentBitmap, r, dstRect, null);

        // Release bitmap memory as soon as possible
        mImageView.clear();
        mBitmap.recycle();
        mBitmap = null;

        currentBitmap.recycle();
        currentBitmap = null;

        mImageView.setImageBitmapResetBase(croppedImage, true);
        mImageView.center(true, true);
        mImageView.mHighlightViews.clear();

        String imgPath = getFilePath(targetUri);
        final String cropPath = imgPath.replace(".", "_crop_image.").trim();
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                saveDrawableToCache(croppedImage, cropPath);
            }

        });
        FileUtils.deleteFile(imgPath);

        Uri cropUri = Uri.fromFile(new File(cropPath));

        Intent intent = new Intent("inline-data");
        intent.putExtra(Constants.CROP_IMAGE_URI, cropUri);
        intent.putExtra(Constants.CROP_IMAGE_PATH, cropPath);
        intent.putExtra(Constants.CROP_REMARK_NAME_KEY, remarkEdit.getEditableText().toString());
        intent.putExtra(Constants.CROP_SAVE_KEY, true);
        if(next){
            
            setResult(RESULT_OK, intent);    
        }else{
            setResult(RESULT_CANCELED, intent);
        }
        
        finish();
    }

    /**
     * 锟斤拷Bitmap锟斤拷锟诫缓锟芥，
     *
     * @param bitmap
     * @param filePath
     * @return void
     * @Title: saveDrawableToCache
     * @date 2012-12-14 锟斤拷锟斤拷9:27:38
     */
    private void saveDrawableToCache(Bitmap bitmap, String filePath) {

        try {
            File file = new File(filePath);

            file.createNewFile();

            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
