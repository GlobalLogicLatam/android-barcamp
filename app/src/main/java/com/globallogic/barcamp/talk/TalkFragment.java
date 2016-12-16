package com.globallogic.barcamp.talk;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.globallogic.barcamp.BaseActivity;
import com.globallogic.barcamp.BaseFragment;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.domain.Talk;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/12/2016
 */
public abstract class TalkFragment extends BaseFragment<TalkPresenter> implements TalkView {

    private static final int PERMISSIONS_CHECK_CAMERA_REQUEST = 1;
    private static final int PERMISSIONS_CHECK_READ_WRITE_REQUEST = 2;

    protected Long talkTime;
    protected AppCompatEditText talkName;
    protected AppCompatEditText talkSpeaker;
    protected AppCompatEditText talkTwitterSpeaker;
    protected AppCompatEditText talkDescription;
    protected AppCompatSpinner roomSpinner;
    private TextView submitButton;
    private View pictureButtonsContainer;
    private View pictureToUploadContainer;
    protected ImageView picture;
    private View cameraButton;
    private ProgressDialog progressDialog;
    private String talkId = null;

    protected abstract TalkPresenter.TalkMode getTalkMode();

    protected abstract String getSubmitButtonText();

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_talk;
    }

    @Override
    protected TalkPresenter buildPresenter() {
        return new TalkPresenter(this, getTalkMode(), talkId);
    }

    @Override
    protected void initView(View view) {
        talkName = (AppCompatEditText) view.findViewById(R.id.talk_name);
        talkSpeaker = (AppCompatEditText) view.findViewById(R.id.talk_speaker);
        talkTwitterSpeaker = (AppCompatEditText) view.findViewById(R.id.talk_twitter_speaker);
        talkDescription = (AppCompatEditText) view.findViewById(R.id.talk_description);
        roomSpinner = (AppCompatSpinner) view.findViewById(R.id.room_spinner);
        submitButton = (TextView) view.findViewById(R.id.submit_button);
        cameraButton = view.findViewById(R.id.camera_button);
        View photoButton = view.findViewById(R.id.photo_button);
        View clearButton = view.findViewById(R.id.clear_picture_button);
        pictureButtonsContainer = view.findViewById(R.id.picture_buttons_container);
        pictureToUploadContainer = view.findViewById(R.id.picture_taken_container);
        picture = (ImageView) view.findViewById(R.id.picture);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Talk talk = createTalk();
                presenter.onSubmitButtonPressed(talk);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermissions();
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkReadWritePermissions();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onClearPicturePressed();
            }
        });
    }

    private class TextWatcherChanges implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Do nothing...
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Do nothing...
        }

        @Override
        public void afterTextChanged(Editable editable) {
            presenter.checkChanges(createTalk());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_CHECK_CAMERA_REQUEST || requestCode == PERMISSIONS_CHECK_READ_WRITE_REQUEST) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                if (requestCode == PERMISSIONS_CHECK_CAMERA_REQUEST) {
                    presenter.onCameraButtonPressed();
                } else {
                    presenter.onPhotoButtonPressed();
                }
            } else {
                // permission denied
                showToast(getString(R.string.permission_denied_message));
            }
        }
    }

    /**
     * Creates a new Talk based on the current information
     * @return
     */
    private Talk createTalk() {
        String name = talkName.getText().toString();
        String speaker = talkSpeaker.getText().toString();
        String description = talkDescription.getText().toString();
        String roomName = (String) roomSpinner.getAdapter().getItem(roomSpinner.getSelectedItemPosition());
        String twitter = "";
        if (!talkTwitterSpeaker.getText().toString().isEmpty()) {
            twitter = talkTwitterSpeaker.getText().toString();
        }
        return new Talk(talkId, talkTime, name, description, roomName, speaker, isDelayed(), twitter);
    }

    protected abstract boolean isDelayed();

    protected void setTalkTime(Long talkTime) {
        this.talkTime = talkTime;
    }

    protected void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    @Override
    public void roomRender(List<String> roomList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.view_simple_spinner_item, roomList);
        adapter.setDropDownViewResource(R.layout.view_simple_spinner_dropdown_item);
        roomSpinner.setAdapter(adapter);
    }

    @Override
    public void renderFields(Talk data) {
        // To be implemented by EditTalkFragment class
    }

    @Override
    public void setSubmitButtonText() {
        submitButton.setText(getSubmitButtonText());
    }

    @Override
    public void startActivityResult(Intent cameraIntent, int takePhotoCode) {
        startActivityForResult(cameraIntent, takePhotoCode);
    }

    @Override
    public void showPicture(String picturePath, BitmapFactory.Options options) {
        displayPictureContainer();

        Bitmap myBitmap = BitmapFactory.decodeFile(picturePath, options);
        picture.setImageBitmap(myBitmap);
    }

    protected void displayPictureContainer() {
        pictureButtonsContainer.setVisibility(View.GONE);
        pictureToUploadContainer.setVisibility(View.VISIBLE);
    }

    protected void hidePictureContainer() {
        pictureButtonsContainer.setVisibility(View.VISIBLE);
        pictureToUploadContainer.setVisibility(View.GONE);
    }

    @Override
    public void showPictureButtons() {
        hidePictureContainer();
        picture.setImageBitmap(null);
    }

    @Override
    public void hideCameraButton() {
        cameraButton.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        getActivity().onBackPressed();
    }

    @Override
    public void showErrorEmptyName() {
        talkName.setError(getString(R.string.error_empty_name));
    }

    @Override
    public void showErrorEmptySpeaker() {
        talkSpeaker.setError(getString(R.string.error_empty_speaker));
    }

    @Override
    public long getTalkTime() {
        return talkTime;
    }

    @Override
    public String getTalkRoomSelected() {
        return null;
    }

    @Override
    public void showSavingDialog() {
        progressDialog = ProgressDialog.show(getContext(), "", getString(R.string.saving), true);
    }

    @Override
    public void dismissSavingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void addTextWatchers() {
        talkName.addTextChangedListener(new TextWatcherChanges());
        talkSpeaker.addTextChangedListener(new TextWatcherChanges());
        talkTwitterSpeaker.addTextChangedListener(new TextWatcherChanges());
        talkDescription.addTextChangedListener(new TextWatcherChanges());
        roomSpinner.setOnItemSelectedListener(presenter);
    }

    @Override
    public void showLostChangesConfirmation(DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.lost_changes_confirmation_message)
                .setPositiveButton(R.string.yes, onClickListener)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void showDeleteImageConfirmation(DialogInterface.OnClickListener positiveListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.delete_image_confirmation_message)
                .setPositiveButton(R.string.yes, positiveListener)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void showDeleteFailure() {
        showToast(getString(R.string.delete_failure_message));
    }

    @Override
    public void closeKeyboard() {
        // Check if no view has focus:
        ((BaseActivity) getActivity()).setKeyboardVisibility(false);
    }

    private void checkCameraPermissions() {
        boolean needPermissionCamera = PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        boolean needPermissionReadWrite = PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (needPermissionCamera) {
            String[] permissions;
            if (needPermissionReadWrite) {
                permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            } else {
                permissions = new String[]{Manifest.permission.CAMERA,};
            }
            requestPermissions(permissions, PERMISSIONS_CHECK_CAMERA_REQUEST);
        } else {
            presenter.onCameraButtonPressed();
        }
    }

    private void checkReadWritePermissions() {
        boolean needPermissionReadWrite = PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (needPermissionReadWrite) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_CHECK_READ_WRITE_REQUEST);
        } else {
            presenter.onPhotoButtonPressed();
        }
    }

}
