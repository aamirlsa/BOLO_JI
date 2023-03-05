package com.boloji.videocallchat.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.boloji.videocallchat.NetworkConnection.ClientApi;
import com.boloji.videocallchat.NetworkConnection.ApiConstant;
import com.boloji.videocallchat.NetworkConnection.PeersConnection;
import com.boloji.videocallchat.NetworkConnection.CustomObserver;
import com.boloji.videocallchat.NetworkConnection.SignallingClient;
import com.boloji.videocallchat.NetworkConnection.AudioCallManger;
import com.boloji.videocallchat.NetworkConnection.WallpaperInterfaces;
import com.boloji.videocallchat.OtherData.Direction;
import com.boloji.videocallchat.OtherData.EmojiAdapter;
import com.boloji.videocallchat.OtherData.ZeroGravityAnimation;
import com.boloji.videocallchat.R;
import com.boloji.videocallchat.ChatsModelClass.ServerModel;
import com.boloji.videocallchat.ChatsModelClass.StrangerMeetModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.Logging;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RendererCommon;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RandomVideoCallActivity extends AppCompatActivity implements View.OnClickListener, SignallingClient.SignalingInterface {

    public static List<org.webrtc.PeerConnection.IceServer> peerIceServersList = new ArrayList();
    MediaConstraints audioConstraints;
    private AudioCallManger audioManager;
    AudioSource audioSource;
    boolean callControlFragmentVisible = true;
    long callWaitingCount;
    long callduration = 0;
    EmojiAdapter emojiAdapter;
    boolean gotUserMedia = false;
    final Handler handlerDuration = new Handler();
    final Handler handlerWaiting = new Handler();
    ImageView imgCameraSwitch, imgDisconnectCall, imgLike, imgMic;
    boolean isFrontCameraOn = true;
    boolean isHangUpCallByButton = false;
    boolean isMicOn = true;
    AudioTrack localAudioTrack;
    org.webrtc.PeerConnection localPeer;
    VideoTrack localVideoTrack;
    SurfaceViewRenderer local_gl_surface_view;
    LottieAnimationView lottieAnimationView;
    PeerConnectionFactory peerConnectionFactory;
    RecyclerView recylerViewSelectEmoji;
    RelativeLayout relBottomContainer, relProgressCenter, relTopContainer, relWaitingMessage;
    SurfaceViewRenderer remote_gl_surface_view;
    EglBase rootEglBase;
    MediaConstraints sdpConstraints;
    int selectedEmoji;
    SignallingClient signallingClient;
    SurfaceTextureHelper surfaceTextureHelper;
    Timer timerDuration,timerWaiting;
    TimerTask timerTaskDuration, timerTaskWaiting;
    int total_video_count;
    TextView txtCallDuration, txtNameFirstLetter, txtNameOfOpponent, txtWaitingTimeCounter;
    VideoCapturer videoCapturerAndroid;
    MediaConstraints videoConstraints;
    VideoSource videoSource;

    private void onAudioManagerDevicesChanged(AudioCallManger.AudioDevice audioDevice, Set<AudioCallManger.AudioDevice> set) {
    }

    @Override
    public void onNewMessage(JSONObject jSONObject) {
    }

    @Override
    public void onNewPeerJoined() {
    }


    private NativeAd mobNativeView;

    private void NativeBinding(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView) {
        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
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
            public void onNativeAdLoaded(com.google.android.gms.ads.nativead.NativeAd nativeAd) {

                boolean isDestroyed = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isDestroyed = isDestroyed();
                }
                if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                    nativeAd.destroy();
                    return;
                }
                if (RandomVideoCallActivity.this.mobNativeView != null) {
                    RandomVideoCallActivity.this.mobNativeView.destroy();
                }
                RandomVideoCallActivity.this.mobNativeView = nativeAd;
                NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.mobnative, null);
                NativeBinding(nativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });
        VideoOptions videoOptions = new VideoOptions.Builder().build();
        com.google.android.gms.ads.nativead.NativeAdOptions adOptions = new com.google.android.gms.ads.nativead.NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
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
    RadioGroup radioGroup;
    View sheetViewblkrep;
    BottomSheetDialog mBottomSheetDialogblkrep;
    ImageView imgflag;

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
                    Toast.makeText(RandomVideoCallActivity.this, "Please Select Report", Toast.LENGTH_SHORT).show();
                    return;
                }
                radioGroup.clearCheck();
                RadioButton radioButton = radioGroup.findViewById(checkedRadioButtonId);
                mBottomSheetDialogblkrep.dismiss();
                Toast.makeText(RandomVideoCallActivity.this, "User is Flagged! Our Team will Take Appropriate Action!", Toast.LENGTH_SHORT).show();
                mBottomSheetDialogblkrep.dismiss();
            }
        });
    }
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_random_video_call);
        showblkrep();
        imgflag = findViewById(R.id.imgflag);
        imgflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialogblkrep.show();
            }
        });
        NativeLoad();
        initializeView();
        setOnClickListenerToViews();
        showProgressForWaiting();
        this.signallingClient = new SignallingClient();
        showWaitingDialog();
        getLoginInfo();
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1 && iArr.length == 2 && iArr[0] == 0 && iArr[1] == 0) {
            start();
        } else {
            finish();
        }
    }
    private void setOnClickListenerToViews() {
        this.remote_gl_surface_view.setOnClickListener(this);
        this.local_gl_surface_view.setOnClickListener(this);
        this.imgDisconnectCall.setOnClickListener(this);
        this.imgLike.setOnClickListener(this);
        this.imgMic.setOnClickListener(this);
        this.imgCameraSwitch.setOnClickListener(this);
    }

    @SuppressLint("WrongConstant")
    private void initializeView() {
        remote_gl_surface_view = (SurfaceViewRenderer) findViewById(R.id.remote_gl_surface_view);
        local_gl_surface_view = (SurfaceViewRenderer) findViewById(R.id.local_gl_surface_view);
        relProgressCenter = (RelativeLayout) findViewById(R.id.relProgressCenter);
        LottieAnimationView lottieAnimationView2 = (LottieAnimationView) findViewById(R.id.videoCallProgress);
        lottieAnimationView = lottieAnimationView2;
        lottieAnimationView.playAnimation();
        lottieAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
            /* class randomlivecall.random.live.call.videocall.livechat.LiveVideoCallActivity.AnonymousClass1 */

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                RandomVideoCallActivity.this.lottieAnimationView.resumeAnimation();
            }
        });
        TextView textView = (TextView) findViewById(R.id.txtNameOfOpponent);
        txtNameOfOpponent = textView;
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        txtCallDuration = (TextView) findViewById(R.id.txtCallDuration);
        txtNameFirstLetter = (TextView) findViewById(R.id.txtNameFirstLetter);
        relBottomContainer = (RelativeLayout) findViewById(R.id.relBottomContainer);
        relTopContainer = (RelativeLayout) findViewById(R.id.relTopContainer);
        imgDisconnectCall = (ImageView) findViewById(R.id.imgDisconnectCall);
        imgMic = (ImageView) findViewById(R.id.imgMic);
        imgCameraSwitch = (ImageView) findViewById(R.id.imgCameraSwitch);
        imgLike = (ImageView) findViewById(R.id.imgLike);
        new LinearLayoutManager(this).setReverseLayout(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylerViewSelectEmoji);
        recylerViewSelectEmoji = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, 0, true));
        EmojiAdapter emojiAdapter2 = new EmojiAdapter(this, ApiConstant.EMOJI_LIST);
        emojiAdapter = emojiAdapter2;
        recylerViewSelectEmoji.setAdapter(emojiAdapter2);
        relWaitingMessage = (RelativeLayout) findViewById(R.id.relWaitingMessage);
        txtWaitingTimeCounter = (TextView) findViewById(R.id.txtWaitingTimeCounter);
    }

    @SuppressLint("WrongConstant")
    private void toggleCallControlFragmentVisibility() {
        this.callControlFragmentVisible = !this.callControlFragmentVisible;
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (this.callControlFragmentVisible) {
            this.relTopContainer.setVisibility(0);
            this.relBottomContainer.setVisibility(0);
        } else {
            this.relTopContainer.setVisibility(8);
            this.relBottomContainer.setVisibility(8);
        }
        beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        beginTransaction.commit();
    }

    @SuppressLint("WrongConstant")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgCameraSwitch:
                boolean z = !this.isFrontCameraOn;
                this.isFrontCameraOn = z;
                this.local_gl_surface_view.setMirror(z);
                this.signallingClient.emitChatSwitchCameramsg(this.isFrontCameraOn);
                ((CameraVideoCapturer) this.videoCapturerAndroid).switchCamera(null);
                return;
            case R.id.imgDisconnectCall:
                this.isHangUpCallByButton = true;
                hangup();
                return;
            case R.id.imgLike:
                if (this.recylerViewSelectEmoji.getVisibility() == 0) {
                    this.recylerViewSelectEmoji.setVisibility(8);
                    return;
                } else {
                    this.recylerViewSelectEmoji.setVisibility(0);
                    return;
                }
            case R.id.imgMic:
                if (this.localAudioTrack == null) {
                    return;
                }
                if (this.isMicOn) {
                    this.isMicOn = false;
                    this.imgMic.setImageResource(R.drawable.icon_mike_bandh);
                    this.localAudioTrack.setEnabled(false);
                    return;
                }
                this.isMicOn = true;
                this.imgMic.setImageResource(R.drawable.icon_mike_chalu);
                this.localAudioTrack.setEnabled(true);
                return;
            case R.id.local_gl_surface_view:
            case R.id.remote_gl_surface_view:
                toggleCallControlFragmentVisibility();
                return;
            default:
                return;
        }
    }

    private void initVideos() {
        EglBase create = EglBase.create();
        this.rootEglBase = create;
        this.local_gl_surface_view.init(create.getEglBaseContext(), null);
        this.local_gl_surface_view.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        this.remote_gl_surface_view.init(this.rootEglBase.getEglBaseContext(), null);
        this.remote_gl_surface_view.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        this.local_gl_surface_view.setZOrderMediaOverlay(true);
        this.local_gl_surface_view.setEnableHardwareScaler(true);
        this.local_gl_surface_view.setEnableHardwareScaler(false);
    }

    public void start() {
        initVideos();
        List<org.webrtc.PeerConnection.IceServer> list = peerIceServersList;
        if (list == null || list.size() == 0) {
            peerIceServersList = new ArrayList();
            peerIceServersList.add(org.webrtc.PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
            peerIceServersList.add(org.webrtc.PeerConnection.IceServer.builder("turn:numb.viagenie.ca").setUsername("webrtc@live.com").setPassword("muazkh").createIceServer());
        }
        this.signallingClient.init(this, getApplicationContext());
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(this).setFieldTrials("WebRTC-IntelVP8/Enabled/").setEnableInternalTracer(true).createInitializationOptions());
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(this.rootEglBase.getEglBaseContext(), true, true);

        this.peerConnectionFactory = PeerConnectionFactory.builder().setOptions(options).setVideoEncoderFactory(defaultVideoEncoderFactory).setVideoDecoderFactory(new DefaultVideoDecoderFactory(this.rootEglBase.getEglBaseContext())).createPeerConnectionFactory();

        this.videoCapturerAndroid = createCameraCapturer(new Camera1Enumerator(false));
        this.audioConstraints = new MediaConstraints();
        this.videoConstraints = new MediaConstraints();
        if (this.videoCapturerAndroid != null) {
            this.surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", this.rootEglBase.getEglBaseContext());
            VideoSource createVideoSource = this.peerConnectionFactory.createVideoSource(this.videoCapturerAndroid);
            this.videoSource = createVideoSource;

        }
        this.localVideoTrack = this.peerConnectionFactory.createVideoTrack("100", this.videoSource);
        AudioSource createAudioSource = this.peerConnectionFactory.createAudioSource(this.audioConstraints);
        this.audioSource = createAudioSource;
        this.localAudioTrack = this.peerConnectionFactory.createAudioTrack("101", createAudioSource);
        VideoCapturer videoCapturer = this.videoCapturerAndroid;
        if (videoCapturer != null) {
            videoCapturer.startCapture(1024, 720, 30);
        }
        this.localVideoTrack.addSink(this.local_gl_surface_view);
        this.local_gl_surface_view.setMirror(true);
        this.remote_gl_surface_view.setMirror(true);
        this.gotUserMedia = true;
        if (this.signallingClient.isInitiator) {
            onTryToStart();
        }
    }

    @Override

    public void onTryToStart() {
        runOnUiThread(new Runnable() {


            public final void run() {
                RandomVideoCallActivity.this.lambda$onTryToStart$0$LiveVideoCallActivity();
            }
        });
    }

    public void lambda$onTryToStart$0$LiveVideoCallActivity() {
        if (!this.signallingClient.isStarted && this.localVideoTrack != null && this.signallingClient.isChannelReady) {
            createPeerConnection();
            this.signallingClient.isStarted = true;
            if (this.signallingClient.isInitiator) {
                doCall();
            }
        }
    }

    private void createPeerConnection() {
        org.webrtc.PeerConnection.RTCConfiguration rTCConfiguration = new org.webrtc.PeerConnection.RTCConfiguration(peerIceServersList);
        rTCConfiguration.tcpCandidatePolicy = org.webrtc.PeerConnection.TcpCandidatePolicy.DISABLED;
        rTCConfiguration.bundlePolicy = org.webrtc.PeerConnection.BundlePolicy.MAXBUNDLE;
        rTCConfiguration.rtcpMuxPolicy = org.webrtc.PeerConnection.RtcpMuxPolicy.REQUIRE;
        rTCConfiguration.continualGatheringPolicy = org.webrtc.PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        rTCConfiguration.keyType = org.webrtc.PeerConnection.KeyType.ECDSA;
        this.localPeer = this.peerConnectionFactory.createPeerConnection(rTCConfiguration, new PeersConnection("localPeerCreation") {


            @Override

            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                Log.i("createPeerConnection", "onIceCandidate");
                RandomVideoCallActivity.this.onIceCandidateReceived(iceCandidate);
            }

            @Override

            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
                RandomVideoCallActivity.this.gotRemoteStream(mediaStream);
                Log.i("createPeerConnection", "onAddStream");
            }

            @Override

            public void onIceConnectionChange(org.webrtc.PeerConnection.IceConnectionState iceConnectionState) {
                super.onIceConnectionChange(iceConnectionState);
                Log.i("createPeerConnection", "onIceConnectionChange");
                if (iceConnectionState != null && iceConnectionState.toString().equals("DISCONNECTED")) {
                    runOnUiThread(new Runnable() {


                        public final void run() {
                            RandomVideoCallActivity.this.hangup();
                        }
                    });
                }
            }
        });
        addStreamToLocalPeer();
    }

    private void addStreamToLocalPeer() {
        MediaStream createLocalMediaStream = this.peerConnectionFactory.createLocalMediaStream("102");
        createLocalMediaStream.addTrack(this.localAudioTrack);
        createLocalMediaStream.addTrack(this.localVideoTrack);
        this.localPeer.addStream(createLocalMediaStream);
    }

    private void doCall() {
        MediaConstraints mediaConstraints = new MediaConstraints();
        this.sdpConstraints = mediaConstraints;
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        this.sdpConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        this.localPeer.createOffer(new CustomObserver("localCreateOffer") {


            @Override

            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                RandomVideoCallActivity.this.localPeer.setLocalDescription(new CustomObserver("localSetLocalDesc"), sessionDescription);
                RandomVideoCallActivity.this.signallingClient.emitMessage(sessionDescription);
            }
        }, this.sdpConstraints);
    }

    private void gotRemoteStream(MediaStream mediaStream) {
        runOnUiThread(new Runnable() {

            public final void run() {
                try {
                    mediaStream.videoTracks.get(0).addSink(remote_gl_surface_view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void lambda$gotRemoteStream$1$LiveVideoCallActivity(VideoTrack videoTrack) {

    }

    public void onIceCandidateReceived(IceCandidate iceCandidate) {
        this.signallingClient.emitIceCandidate(iceCandidate);
    }

    @Override
    public void onCreatedRoom() {
        this.signallingClient.emitMessage("got user media");
    }

    @Override
    public void onJoinedRoom() {
        this.signallingClient.emitMessage("got user media");
        startTimer();
    }

    @Override
    public void onNewEmoji(JSONObject jSONObject) {
        if (jSONObject != null) {
            try {
                showEmojiToOpponent(jSONObject.getString("emoji_name"), jSONObject.getInt("emoji_count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCameraSwitchNode(JSONObject jSONObject) {
        if (jSONObject != null) {
            try {
                this.remote_gl_surface_view.setMirror(jSONObject.getBoolean("cameraFlag"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRemoteHangUp(String str) {
        runOnUiThread(new Runnable() {


            public final void run() {
                RandomVideoCallActivity.this.hangup();
            }
        });
    }

    @Override
    public void onOfferReceived(JSONObject jSONObject) {
        runOnUiThread(new Runnable() {


            public final void run() {
                if (!signallingClient.isInitiator && !signallingClient.isStarted) {
                    onTryToStart();
                }
                try {
                    if (localPeer != null) {
                        localPeer.setRemoteDescription(new CustomObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.OFFER, jSONObject.getString("sdp")));
                        doAnswer();
                        updateVideoViews(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void doAnswer() {
        this.localPeer.createAnswer(new CustomObserver("localCreateAns") {


            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                if (RandomVideoCallActivity.this.localPeer != null) {
                    Log.e("testing", "doAnswer : onCreateSuccess " + sessionDescription);
                    RandomVideoCallActivity.this.localPeer.setLocalDescription(new CustomObserver("localSetLocal"), sessionDescription);
                    RandomVideoCallActivity.this.signallingClient.emitMessage(sessionDescription);
                }
            }
        }, new MediaConstraints());
    }

    @Override
    public void onAnswerReceived(JSONObject jSONObject) {
        try {
            this.localPeer.setRemoteDescription(new CustomObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.fromCanonicalForm(jSONObject.getString("type").toLowerCase()), jSONObject.getString("sdp")));
            updateVideoViews(true);
            startTimer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onIceCandidateReceived(final JSONObject jSONObject) {
        if (jSONObject != null) {
            runOnUiThread(new Runnable() {


                public void run() {
                    try {
                        String string = jSONObject.getString("nikename");
                        ApiConstant.opponet_device_id = jSONObject.getString("device_id");
                        if (string != null && !string.equals("")) {
                            RandomVideoCallActivity.this.txtNameFirstLetter.setText(String.valueOf(string.charAt(0)).toUpperCase());
                        }
                        RandomVideoCallActivity.this.txtNameOfOpponent.setText(string);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (this.localPeer != null) {
            try {
                this.localPeer.addIceCandidate(new IceCandidate(jSONObject.getString("id"), jSONObject.getInt("label"), jSONObject.getString("candidate")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateVideoViews(boolean z) {
        runOnUiThread(new Runnable() {


            public final void run() {
                SurfaceViewRenderer surfaceViewRenderer = local_gl_surface_view;
                if (surfaceViewRenderer != null) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) surfaceViewRenderer.getLayoutParams();
                    if (z) {
                        layoutParams.height = dpToPx(150);
                        layoutParams.width = dpToPx(100);
                        layoutParams.setMargins(10, 10, 10, 10);
                        layoutParams.gravity = GravityCompat.END;
                    } else {
                        layoutParams = new FrameLayout.LayoutParams(-1, -1);
                    }
                    local_gl_surface_view.setLayoutParams(layoutParams);
                }
            }
        });
    }


    @SuppressLint("WrongConstant")
    public void hangup() {


        try {
            stoptimertask();
            stoptimertaskForWaiting();
            if (this.localPeer != null) {
                this.localPeer.close();
                this.localPeer = null;
            }
            this.signallingClient.close();
            if (this.local_gl_surface_view != null) {
                this.local_gl_surface_view.release();
                this.local_gl_surface_view = null;
            }
            if (this.remote_gl_surface_view != null) {
                this.remote_gl_surface_view.release();
                this.remote_gl_surface_view = null;
            }
            if (this.videoCapturerAndroid != null) {
                this.videoCapturerAndroid.stopCapture();
            }
            if (this.audioManager != null) {
                this.audioManager.stop();
                this.audioManager = null;
            }
            PeerConnectionFactory.stopInternalTracingCapture();

            onBackPressed1();
        } catch (Exception e) {
            stoptimertask();
            stoptimertaskForWaiting();
            e.printStackTrace();
            onBackPressed1();
        }

    }


    public void onBackPressed1() {
        startActivity(new Intent(this, CallStrangerActivity.class));

    }

    @Override
    public void onDestroy() {
        Thread.setDefaultUncaughtExceptionHandler(null);
        if (!this.isHangUpCallByButton) {
            hangup();
        }
        Timer timer = this.timerDuration;
        if (timer != null) {
            timer.cancel();
            this.timerDuration = null;
        }
        super.onDestroy();
    }

    public int dpToPx(int i) {
        return Math.round(((float) i) * (getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator cameraEnumerator) {
        CameraVideoCapturer createCapturer;
        String[] deviceNames = cameraEnumerator.getDeviceNames();
        Logging.d(ApiConstant.TAG, "Looking for front facing cameras.");
        for (String str : deviceNames) {
            if (cameraEnumerator.isFrontFacing(str)) {
                Logging.d(ApiConstant.TAG, "Creating front facing camera capturer.");
                CameraVideoCapturer createCapturer2 = cameraEnumerator.createCapturer(str, null);
                if (createCapturer2 != null) {
                    return createCapturer2;
                }
            }
        }
        for (String str2 : deviceNames) {
            if (!(cameraEnumerator.isFrontFacing(str2) || (createCapturer = cameraEnumerator.createCapturer(str2, null)) == null)) {
                return createCapturer;
            }
        }
        return null;
    }


    public void getLoginInfo() {
        ((WallpaperInterfaces) ClientApi.getClient(getApplicationContext()).create(WallpaperInterfaces.class)).getLoginInformation(ApiConstant.getDeviceId(), ApiConstant.getUserName(this)).enqueue(new Callback<StrangerMeetModel>() {


            @Override
            public void onFailure(Call<StrangerMeetModel> call, Throwable th) {
                th.printStackTrace();
            }

            @Override
            public void onResponse(Call<StrangerMeetModel> call, Response<StrangerMeetModel> response) {
                StrangerMeetModel body = response.body();
                if (body != null) {

                    RandomVideoCallActivity.this.total_video_count = body.getVideo_count();
                    body.getUser_id();
                    RandomVideoCallActivity.this.signallingClient.roomName = body.getRoom_id();


                    ArrayList<ServerModel> stun_server = body.getStun_server();
                    if (stun_server != null && stun_server.size() > 0) {
                        for (int i = 0; i < stun_server.size(); i++) {
                            ServerModel stunServerModel = stun_server.get(i);
                            peerIceServersList.add(org.webrtc.PeerConnection.IceServer.builder(stunServerModel.getUrl()).setUsername(stunServerModel.getUnm()).setPassword(stunServerModel.getPsw()).createIceServer());

                        }
                    }
                    if (ContextCompat.checkSelfPermission(RandomVideoCallActivity.this, "android.permission.CAMERA") == 0 && ContextCompat.checkSelfPermission(RandomVideoCallActivity.this, "android.permission.RECORD_AUDIO") == 0) {
                        RandomVideoCallActivity.this.start();


                    } else {
                        ActivityCompat.requestPermissions(RandomVideoCallActivity.this, new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO"}, 1);
                    }
                }
            }
        });
    }

    public void initializeTimerTask() {
        this.timerTaskDuration = new TimerTask() {
            /* class randomlivecall.random.live.call.videocall.livechat.LiveVideoCallActivity.AnonymousClass11 */

            public void run() {
                RandomVideoCallActivity.this.handlerDuration.post(new Runnable() {


                    public void run() {
                        RandomVideoCallActivity.this.callduration++;
                        RandomVideoCallActivity.this.txtCallDuration.setText(RandomVideoCallActivity.this.formatHHMMSS(RandomVideoCallActivity.this.callduration));
                    }
                });
            }
        };
    }

    public String formatHHMMSS(long j) {
        int i = ((int) j) % 60;
        long j2 = (j - ((long) i)) / 60;
        long j3 = j2 % 60;
        return "" + String.format("%02d", Long.valueOf((j2 - j3) / 60)) + ":" + String.format("%02d", Long.valueOf(j3)) + ":" + String.format("%02d", Integer.valueOf(i));
    }

    public void startTimer() {
        runOnUiThread(new Runnable() {


            public final void run() {
                RandomVideoCallActivity.this.hideWaitingDialog();
            }
        });
        runOnUiThread(new Runnable() {


            public final void run() {
                RandomVideoCallActivity.this.hideProgressForWaiting();
            }
        });
        this.audioManager = AudioCallManger.create(getApplicationContext());
        Log.d(ApiConstant.TAG, "Starting the audio manager...");
        this.audioManager.start(new AudioCallManger.AudioManagerEvents() {


            @Override

            public void onAudioDeviceChanged(AudioCallManger.AudioDevice audioDevice, Set<AudioCallManger.AudioDevice> set) {
                RandomVideoCallActivity.this.onAudioManagerDevicesChanged(audioDevice, set);
            }
        });
        Timer timer = this.timerDuration;
        if (timer != null) {
            timer.cancel();
            this.timerDuration = null;
        }
        TimerTask timerTask = this.timerTaskDuration;
        if (timerTask != null) {
            timerTask.cancel();
            this.timerTaskDuration = null;
        }
        this.callduration = 0;
        this.timerDuration = new Timer();
        initializeTimerTask();
        this.timerDuration.schedule(this.timerTaskDuration, 0, 1000);
    }

    public void stoptimertask() {
        Timer timer = this.timerDuration;
        if (timer != null) {
            timer.cancel();
            this.timerDuration = null;
        }
        TimerTask timerTask = this.timerTaskDuration;
        if (timerTask != null) {
            timerTask.cancel();
            this.timerTaskDuration = null;
        }
    }

    public void flyEmoji(String str) {
        ZeroGravityAnimation zeroGravityAnimation = new ZeroGravityAnimation();
        zeroGravityAnimation.setCount(1);
        zeroGravityAnimation.setScalingFactor(0.2f);
        zeroGravityAnimation.setOriginationDirection(Direction.BOTTOM);
        zeroGravityAnimation.setDestinationDirection(Direction.TOP);
        zeroGravityAnimation.setImage(str);
        zeroGravityAnimation.setAnimationListener(new Animation.AnimationListener() {


            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        zeroGravityAnimation.play(this, (ViewGroup) findViewById(R.id.animation_holder));
    }

    public void emojiSelect(int i) {
        this.recylerViewSelectEmoji.setVisibility(View.GONE);
        this.selectedEmoji = i;
        if (ApiConstant.EMOJI_LIST != null && ApiConstant.EMOJI_LIST.size() > 0) {
            Picasso picasso = Picasso.get();
            picasso.load("file:///android_asset/emojis/" + ApiConstant.EMOJI_LIST.get(this.selectedEmoji)).into(this.imgLike);
            this.signallingClient.emitChatEmoji(ApiConstant.EMOJI_LIST.get(this.selectedEmoji), ApiConstant.getEmojiCounter(this));
        }
        showEmojiToOpponent(ApiConstant.EMOJI_LIST.get(this.selectedEmoji), ApiConstant.getEmojiCounter(this));
    }

    public void showEmojiToOpponent(final String str, final int i) {
        runOnUiThread(new Runnable() {


            public void run() {
                for (int i = 0; i < i; i++) {
                    RandomVideoCallActivity.this.flyEmoji(str);
                }
            }
        });
    }

    public void showProgressForWaiting() {
        this.relProgressCenter.setVisibility(View.VISIBLE);
    }

    public void hideProgressForWaiting() {
        this.relProgressCenter.setVisibility(View.GONE);
    }


    public void showWaitingDialog() {
        Log.i("WaitingDialog", "showWaitingDialog");
        this.relWaitingMessage.setVisibility(View.VISIBLE);
        startTimerForCallWaiting();
    }

    public void hideWaitingDialog() {
        Log.i("WaitingDialog", "hideWaitingDialog");
        this.relWaitingMessage.setVisibility(View.GONE);
        stoptimertaskForWaiting();
    }

    public void startTimerForCallWaiting() {
        Timer timer = this.timerWaiting;
        if (timer != null) {
            timer.cancel();
            this.timerWaiting = null;
        }
        TimerTask timerTask = this.timerTaskWaiting;
        if (timerTask != null) {
            timerTask.cancel();
            this.timerTaskWaiting = null;
        }
        this.callWaitingCount = (long) ApiConstant.getRandomNoBetweenTwoNo(45, 60);
        this.timerWaiting = new Timer();
        initializeTimerTaskForWaiting();
        this.timerWaiting.schedule(this.timerTaskWaiting, 0, 1000);
    }

    public void initializeTimerTaskForWaiting() {
        this.timerTaskWaiting = new TimerTask() {
            /* class randomlivecall.random.live.call.videocall.livechat.LiveVideoCallActivity.AnonymousClass15 */

            public void run() {
                RandomVideoCallActivity.this.handlerWaiting.post(new Runnable() {


                    public void run() {
                        if (RandomVideoCallActivity.this.callWaitingCount != 0) {
                            RandomVideoCallActivity.this.callWaitingCount--;
                        } else {
                            RandomVideoCallActivity.this.stoptimertaskForWaiting();
                            RandomVideoCallActivity.this.startLocalVideActivity();
                        }
                        RandomVideoCallActivity.this.txtWaitingTimeCounter.setText(String.format("%02d", Long.valueOf(RandomVideoCallActivity.this.callWaitingCount)));
                    }
                });
            }
        };
    }

    public void stoptimertaskForWaiting() {
        Timer timer = this.timerWaiting;
        if (timer != null) {
            timer.cancel();
            this.timerWaiting = null;
        }
        TimerTask timerTask = this.timerTaskWaiting;
        if (timerTask != null) {
            timerTask.cancel();
            this.timerTaskWaiting = null;
        }
    }

    public void startLocalVideActivity() {


        hangup("");
    }

    private void hangup(String str) {
        try {
            stoptimertask();
            stoptimertaskForWaiting();
            if (this.localPeer != null) {
                this.localPeer.close();
                this.localPeer = null;
            }
            this.signallingClient.close();
            if (this.local_gl_surface_view != null) {
                this.local_gl_surface_view.release();
                this.local_gl_surface_view = null;
            }
            if (this.remote_gl_surface_view != null) {
                this.remote_gl_surface_view.release();
                this.remote_gl_surface_view = null;
            }
            if (this.videoCapturerAndroid != null) {
                this.videoCapturerAndroid.stopCapture();
            }
            if (this.audioManager != null) {
                this.audioManager.stop();
                this.audioManager = null;
            }
            PeerConnectionFactory.stopInternalTracingCapture();
            Toast.makeText(getApplicationContext(), "Opponent not found..try again!!", Toast.LENGTH_LONG).show();
            onBackPressed1();

        } catch (Exception e) {
            stoptimertask();
            stoptimertaskForWaiting();
            onBackPressed1();
            e.printStackTrace();
        }
        finish();
    }
}
