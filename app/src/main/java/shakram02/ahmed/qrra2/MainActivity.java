/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shakram02.ahmed.qrra2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private static final int RC_BARCODE_CAPTURE = 9001;
    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private String ttsEngineName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        TextToSpeech tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            }
        });

        final List<TextToSpeech.EngineInfo> engines = tts.getEngines();
        tts.shutdown();

        createTtsEngineSelection(engines);

        findViewById(R.id.read_barcode).setOnClickListener(this);
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());
            intent.putExtra(BarcodeCaptureActivity.TtsService, ttsEngineName);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }

    }


    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RC_BARCODE_CAPTURE) {
//            if (resultCode == CommonStatusCodes.SUCCESS) {
//                if (data != null) {
//                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                    statusMessage.setText(R.string.barcode_success);
//                    barcodeValue.setText(barcode.displayValue);
//                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
//                } else {
//                    statusMessage.setText(R.string.barcode_failure);
//                    Log.d(TAG, "No barcode captured, intent data is null");
//                }
//            } else {
//                statusMessage.setText(String.format(getString(R.string.barcode_error),
//                        CommonStatusCodes.getStatusCodeString(resultCode)));
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createTtsEngineSelection(final List<TextToSpeech.EngineInfo> engines) {
        final RadioGroup engineListView = findViewById(R.id.engine_list);

        for (int i = 0; i < engines.size(); i++) {
            String engineName = engines.get(i).label;
            RadioButton radioButton = new RadioButton(this);

            radioButton.setText(engineName);
            radioButton.setTag(i);
            radioButton.setId(i);

            // Default engine
            if (i == 0) {
                radioButton.setChecked(true);
                ttsEngineName = engineName;
            }

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    ttsEngineName = engines.get(index).name;
                }
            });

            engineListView.addView(radioButton);
        }
    }
}
