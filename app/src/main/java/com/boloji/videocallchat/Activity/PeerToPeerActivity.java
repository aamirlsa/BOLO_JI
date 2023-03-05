package com.boloji.videocallchat.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nhancv.webrtcpeer.rtc_plugins.ProxyRenderer;
import com.boloji.videocallchat.NetworkConnection.PeerToPeerConnection;
import com.boloji.videocallchat.NetworkConnection.PeerToPeerView;
import com.boloji.videocallchat.NetworkConnection.SocketConfig;
import com.boloji.videocallchat.OtherActivities.AppCheck;
import com.boloji.videocallchat.OtherData.ReSchedualCall;
import com.boloji.videocallchat.OtherData.VariableData;
import com.boloji.videocallchat.R;
import com.boloji.videocallchat.TemporaryData.TempSharedpref;
import com.boloji.videocallchat.Configuration.WebConstantData;
import com.wang.avi.AVLoadingIndicatorView;
import org.json.JSONObject;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.EglBase;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;
import java.util.Locale;
import java.util.Random;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

public class PeerToPeerActivity extends MvpActivity<PeerToPeerView, PeerToPeerConnection> implements PeerToPeerView {

    private static final String TAG = "FaceToFaceCallActivity";
    int AudioVol = 1;
    private String[] PERMISSIONS = {"android.permission.CAMERA", "android.permission.RECORD_AUDIO"};
    int TotalSec = 0;
    AudioManager audioManager;
    private boolean isGranted;
    private boolean isSwappedFeeds;
    private ProxyRenderer localProxyRenderer;
    BottomSheetDialog mBottomSheetDialogblkrep;
    RadioGroup radioGroup;
    private ProxyRenderer remoteProxyRenderer;
    private EglBase rootEglBase;
    int seconds = 0;
    View sheetViewblkrep;
    CountDownTimer timerVideo;
    int vol = 1;
    RelativeLayout FLFinding, RLMainWait,llCallArea;
    AVLoadingIndicatorView avi;
    AppCompatImageView btOne2OneCallDis, btSwitchCamera, btnCallEnd1, like, like1, heart1, thumb1;
    Chronometer chronometer;
    ImageView imgflag, imgheartt,btMute,voiceBtn;
    LinearLayout linearLayout3, linearLayout5, buttonsCallContainer;
    CircleImageView ownimg;
    LinearLayout ssss1;
    TextView textView2;
    CircleImageView twoimgg;
    SurfaceViewRenderer vGLSurfaceViewCallFull, vGLSurfaceViewCallPip;
    boolean is_call_success = false;

    private void showblkrep() {

        this.mBottomSheetDialogblkrep = new BottomSheetDialog(this, R.style.TransparentDialog);
        View inflate = getLayoutInflater().inflate(R.layout.bottom_vidclflgin, (ViewGroup) null);
        this.sheetViewblkrep = inflate;
        this.mBottomSheetDialogblkrep.setContentView(inflate);
        RadioGroup radioGroup2 =  this.sheetViewblkrep.findViewById(R.id.radioGroup);
        this.radioGroup = radioGroup2;
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton =  sheetViewblkrep.findViewById(i);
            }
        });
        EditText editText = this.sheetViewblkrep.findViewById(R.id.editText);
        (this.sheetViewblkrep.findViewById(R.id.txtcn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialogblkrep.dismiss();
            }
        });
        ((TextView) this.sheetViewblkrep.findViewById(R.id.txtok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Toast.makeText(PeerToPeerActivity.this, "Please Select Report", Toast.LENGTH_SHORT).show();
                    return;
                }
                radioGroup.clearCheck();
                RadioButton radioButton = radioGroup.findViewById(checkedRadioButtonId);
                mBottomSheetDialogblkrep.dismiss();
                Toast.makeText(PeerToPeerActivity.this, "User is Flagged! Our Team will Take Appropriate Action!", Toast.LENGTH_SHORT).show();
                mBottomSheetDialogblkrep.dismiss();
            }
        });
    }

    public void btMicClick() {
        try {
            if (this.vol == 0) {
                this.vol = 1;
                btMute.setImageResource(R.drawable.icon_speaker);
                this.audioManager.setMicrophoneMute(false);
                ((PeerToPeerConnection) this.presenter).peerConnectionClient.setAudioEnabled(true);
                return;
            }
            this.vol = 0;
            btMute.setImageResource(R.drawable.icon_mike_bandh);
            this.audioManager.setMicrophoneMute(true);
            ((PeerToPeerConnection) this.presenter).peerConnectionClient.setAudioEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCameraSwitch() {
        if (((PeerToPeerConnection) this.presenter).peerConnectionClient != null) {
            ((PeerToPeerConnection) this.presenter).peerConnectionClient.switchCamera();
        }
    }

    private NativeAd mobNativeView;

    private void NativeBinding(NativeAd nativeAd, NativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }

    public void NativeShow(final FrameLayout frameLayout) {
        AdLoader.Builder builder = new AdLoader.Builder(getApplication(), getString(R.string.AdMob_Native));
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {

                boolean isDestroyed = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isDestroyed = isDestroyed();
                }
                if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                    nativeAd.destroy();
                    return;
                }
                if (PeerToPeerActivity.this.mobNativeView != null) {
                    PeerToPeerActivity.this.mobNativeView.destroy();
                }
                PeerToPeerActivity.this.mobNativeView = nativeAd;
                NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.mobnative, null);
                NativeBinding(nativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });
        VideoOptions videoOptions = new VideoOptions.Builder().build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());

    }

    public void NativeLoad() {
        NativeShow((FrameLayout) findViewById(R.id.mobadslayout));

    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_peer_to_peer);

        NativeLoad();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        FLFinding = findViewById(R.id.FLFinding);
        RLMainWait = findViewById(R.id.RLMainWait);
        avi = findViewById(R.id.avi);
        btMute = findViewById(R.id.btMute);
        btOne2OneCallDis = findViewById(R.id.btOne2OneCallDis);
        btSwitchCamera = findViewById(R.id.btSwitchCamera);
        btnCallEnd1 = findViewById(R.id.btnCallEnd1);
        buttonsCallContainer = findViewById(R.id.buttons_call_container);
        chronometer = findViewById(R.id.chronometer);
        heart1 = findViewById(R.id.heart1);
        imgflag = findViewById(R.id.imgflag);
        imgheartt = findViewById(R.id.imgheartt);
        like = findViewById(R.id.like);
        like1 = findViewById(R.id.like1);
        linearLayout3 = findViewById(R.id.linearLayout3);
        linearLayout5 = findViewById(R.id.linearLayout5);
        llCallArea = findViewById(R.id.llCallArea);

        ownimg = findViewById(R.id.ownimg);
        ssss1 = findViewById(R.id.ssss1);
        textView2 = findViewById(R.id.textView2);
        thumb1 = findViewById(R.id.thumb1);
        twoimgg = findViewById(R.id.twoimgg);
        vGLSurfaceViewCallFull = findViewById(R.id.vGLSurfaceViewCallFull);
        vGLSurfaceViewCallPip = findViewById(R.id.vGLSurfaceViewCallPip);
        voiceBtn = findViewById(R.id.voice_btn);

        btOne2OneCallDis.setVisibility(View.GONE);
        btOne2OneCallDis.setOnClickListener(new View.OnClickListener() {


            public final void onClick(View view) {
                lambda$onCreate$0$FaceToFaceCallActivity(view);
            }
        });
        btMute.setOnClickListener(new View.OnClickListener() {


            public final void onClick(View view) {
                lambda$onCreate$1$FaceToFaceCallActivity(view);
            }
        });
        btSwitchCamera.setOnClickListener(new View.OnClickListener() {


            public final void onClick(View view) {
                lambda$onCreate$2$FaceToFaceCallActivity(view);
            }
        });
        imgheartt.startAnimation(AnimationUtils.loadAnimation(this, R.anim.threeanim));

        timerVideo = new CountDownTimer(20000, 1000) {


            public void onTick(long j) {
            }

            public void onFinish() {
                ((PeerToPeerConnection) presenter).disconnect();
            }
        }.start();
        if (!WebConstantData.hasPermissions(this, this.PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, this.PERMISSIONS, 1);
        } else {
            this.isGranted = true;
        }
        showblkrep();
        imgflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialogblkrep.show();
            }
        });
        like1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like.setVisibility(View.VISIBLE);
                like.setImageResource(R.drawable.icon_likelive);
                Animation loadAnimation = AnimationUtils.loadAnimation(PeerToPeerActivity.this, R.anim.twoanim);
                loadAnimation.setAnimationListener(new Animation.AnimationListener() {
                    /* class com.gtu.newlivevideocall.VideoCallRoom.AnonymousClass6.AnonymousClass1 */

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        like.setVisibility(View.GONE);
                    }
                });
                like.startAnimation(loadAnimation);
            }
        });
        heart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like.setVisibility(View.VISIBLE);
                like.setImageResource(R.drawable.icon_thumblive);
                Animation loadAnimation = AnimationUtils.loadAnimation(PeerToPeerActivity.this, R.anim.twoanim);
                loadAnimation.setAnimationListener(new Animation.AnimationListener() {


                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        like.setVisibility(View.GONE);
                    }
                });
                like.startAnimation(loadAnimation);
            }
        });
        thumb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like.setVisibility(View.VISIBLE);
                like.setImageResource(R.drawable.icon_claplive);
                Animation loadAnimation = AnimationUtils.loadAnimation(PeerToPeerActivity.this, R.anim.twoanim);
                loadAnimation.setAnimationListener(new Animation.AnimationListener() {


                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        like.setVisibility(View.GONE);
                    }
                });
                like.startAnimation(loadAnimation);
            }
        });
    }

    public void lambda$onCreate$0$FaceToFaceCallActivity(View view) {
        ((PeerToPeerConnection) this.presenter).disconnect();
    }

    public void lambda$onCreate$1$FaceToFaceCallActivity(View view) {
        btMicClick();
    }

    public void lambda$onCreate$2$FaceToFaceCallActivity(View view) {
        onCameraSwitch();
    }

    @Override
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        CallService();
        init();
    }

    public void CallService() {
        SocketConfig.getInstance().registerClient(this);
        SocketConfig.getInstance().SocketConnect();
    }


    public void init() {
        setVolumeControlStream(0);
        this.localProxyRenderer = new ProxyRenderer();
        this.remoteProxyRenderer = new ProxyRenderer();
        this.rootEglBase = EglBase.create();
        vGLSurfaceViewCallFull.init(this.rootEglBase.getEglBaseContext(), null);
        vGLSurfaceViewCallFull.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        vGLSurfaceViewCallFull.setEnableHardwareScaler(true);
        vGLSurfaceViewCallFull.setMirror(true);
        vGLSurfaceViewCallPip.init(this.rootEglBase.getEglBaseContext(), null);
        vGLSurfaceViewCallPip.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        vGLSurfaceViewCallPip.setEnableHardwareScaler(true);
        vGLSurfaceViewCallPip.setMirror(true);
        vGLSurfaceViewCallPip.setZOrderMediaOverlay(true);
        vGLSurfaceViewCallPip.setOnClickListener(new View.OnClickListener() {


            public final void onClick(View view) {
                lambda$init$3$FaceToFaceCallActivity(view);
            }
        });
        setSwappedFeeds(true);
        ((PeerToPeerConnection) this.presenter).connectServer();
    }

    public void lambda$init$3$FaceToFaceCallActivity(View view) {
        setSwappedFeeds(!this.isSwappedFeeds);
    }

    @Override
    public void setSwappedFeeds(boolean z) {
        String str = TAG;
        Log.e(str, "setSwappedFeeds: " + z);
        this.isSwappedFeeds = z;
        ProxyRenderer proxyRenderer = this.localProxyRenderer;

        proxyRenderer.setTarget(z ? vGLSurfaceViewCallFull : vGLSurfaceViewCallPip);
        ProxyRenderer proxyRenderer2 = this.remoteProxyRenderer;

        proxyRenderer2.setTarget(z ? vGLSurfaceViewCallPip : vGLSurfaceViewCallFull);
        vGLSurfaceViewCallFull.setMirror(z);
        vGLSurfaceViewCallPip.setMirror(!z);
    }

    @Override
    public void socketConnect(boolean z) {
        if (!z) {
            onBackPressed1();

        } else {

            ((PeerToPeerConnection) this.presenter).register(getIntent().getStringExtra("from"));
        }
    }

    @Override
    public void disconnect() {
        this.localProxyRenderer.setTarget(null);
        if (vGLSurfaceViewCallFull != null) {
            Log.i("FaceToFacePresenter1", "release");
            vGLSurfaceViewCallFull.release();
        }
        Log.i("FaceToFacePresenter1", "direct");
        ChronometerStop();
        StopFindUser();
    }

    public void ChronometerStop() {
        chronometer.stop();
        long elapsedRealtime = SystemClock.elapsedRealtime() - chronometer.getBase();
        Log.e("Timer", " " + elapsedRealtime);
        int i = (int) (elapsedRealtime / 3600000);
        long j = elapsedRealtime - ((long) (3600000 * i));
        int i2 = ((int) j) / 60000;
        this.seconds = ((int) (j - ((long) (60000 * i2)))) / 1000;
        VariableData.TimeCounter = String.format(Locale.getDefault(), "%02d : %02d : %02d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(this.seconds));
        this.TotalSec = (i * 60 * 60) + (i2 * 60) + this.seconds;
    }

    public void startCall() {
        Log.e("OnetoOne", "=startCall");
        if (Build.VERSION.SDK_INT < 23 || this.isGranted) {

            ((PeerToPeerConnection) this.presenter).startCall();
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 1) {
            return;
        }
        if (iArr.length <= 0 || iArr[0] != 0) {
            this.isGranted = false;
            return;
        }
        this.isGranted = true;
        ((PeerToPeerConnection) this.presenter).startCall();
    }

    @Override
    public PeerToPeerConnection createPresenter() {
        return new PeerToPeerConnection(getApplication());
    }


    public void onBackPressed1() {
        ((PeerToPeerConnection) this.presenter).disconnect();

        if (is_call_success) {
            startActivity(new Intent(PeerToPeerActivity.this, CallStrangerActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "Opponent not found..try server 2 call!!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(PeerToPeerActivity.this, CallStrangerActivity.class));

        }

    }

    public void StopFindUser() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("from", AppCheck.getInstance().appPrefs.getNewRegId());
            jSONObject.put("package", TempSharedpref.getString(getApplicationContext(), TempSharedpref.PACKAGE));
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("en", "video_call_mini_app_stopfind");
            jSONObject2.put("data", jSONObject);
            SocketConfig.getInstance().emitCall(jSONObject2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logAndToast(String str) {
        if (str != null) {
            Log.e(TAG, str);
        }
    }

    @Override
    public VideoCapturer createVideoCapturer() {
        VideoCapturer videoCapturer;
        if (!useCamera2()) {
            videoCapturer = createCameraCapturer(new Camera1Enumerator(captureToTexture()));
        } else if (!captureToTexture()) {
            return null;
        } else {
            videoCapturer = createCameraCapturer(new Camera2Enumerator(this));
        }
        if (videoCapturer == null) {
            return null;
        }
        return videoCapturer;
    }

    @Override
    public EglBase.Context getEglBaseContext() {
        return this.rootEglBase.getEglBaseContext();
    }

    @Override
    public VideoRenderer.Callbacks getLocalProxyRenderer() {
        return this.localProxyRenderer;
    }

    @Override
    public VideoRenderer.Callbacks getRemoteProxyRenderer() {
        return this.remoteProxyRenderer;
    }

    @Override
    public void registerStatus(boolean z) {
        if (z && !getIntent().getStringExtra("to").equalsIgnoreCase("")) {
            transactionToCalling(getIntent().getStringExtra("from"), getIntent().getStringExtra("to"), true);
        }
    }

    @Override
    public void transactionToCalling(String str, String str2, boolean z) {
        ((PeerToPeerConnection) this.presenter).initPeerConfig(str, str2, z);
        Log.e("OnetoOne", "=transactionToCalling");
        startCall();
    }

    @Override
    public void incomingCalling(String str) {

        Log.e("OnetoOne", "=Incomming");
        transactionToCalling(str, getIntent().getStringExtra("from"), false);
    }

    @Override
    public void stopCalling() {
        onBackPressed1();
    }

    @Override
    public void startCallIng() {
        this.timerVideo.cancel();
        llCallArea.setVisibility(View.VISIBLE);
        btOne2OneCallDis.setVisibility(View.VISIBLE);
        RLMainWait.setVisibility(View.GONE);
        ReSchedualCall.runOnUi(new Action1() {


            @Override
            public final void call(Object obj) {
                lambda$startCallIng$4$FaceToFaceCallActivity(obj);
            }
        });
    }

    public void lambda$startCallIng$4$FaceToFaceCallActivity(Object obj) {
        ChronometerSet();
    }

    public void ChronometerSet() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        is_call_success = true;
        Log.e("Timer", "start");
        new Random().nextInt(21);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {


            public void onChronometerTick(Chronometer chronometer) {
                long elapsedRealtime = SystemClock.elapsedRealtime() - chronometer.getBase();
                int i = (int) (elapsedRealtime / 3600000);
                long j = elapsedRealtime - ((long) (3600000 * i));
                int i2 = ((int) j) / 60000;
                VariableData.TimeCounter = String.format(Locale.getDefault(), "%02d : %02d : %02d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(((int) (j - ((long) (60000 * i2)))) / 1000));
            }
        });
    }

    private boolean useCamera2() {
        return Camera2Enumerator.isSupported(this) && ((PeerToPeerConnection) this.presenter).getDefaultConfig().isUseCamera2();
    }

    private boolean captureToTexture() {
        return ((PeerToPeerConnection) this.presenter).getDefaultConfig().isCaptureToTexture();
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator cameraEnumerator) {
        CameraVideoCapturer createCapturer;
        CameraVideoCapturer createCapturer2;
        String[] deviceNames = cameraEnumerator.getDeviceNames();
        for (String str : deviceNames) {
            if (cameraEnumerator.isFrontFacing(str) && (createCapturer2 = cameraEnumerator.createCapturer(str, null)) != null) {
                return createCapturer2;
            }
        }
        for (String str2 : deviceNames) {
            if (!(cameraEnumerator.isFrontFacing(str2) || (createCapturer = cameraEnumerator.createCapturer(str2, null)) == null)) {
                return createCapturer;
            }
        }
        return null;
    }
}
