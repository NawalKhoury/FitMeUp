package com.example.fitmeup;

// Import necessary TensorFlow Lite packages
import org.tensorflow.lite.Interpreter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

// Define a helper class to load the model and make predictions
public class AIModelHelper {
    private Interpreter interpreter;

    // Constructor: Load the TensorFlow Lite model
    public AIModelHelper(Context context) {
        try {
            interpreter = new Interpreter(loadModelFile(context));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load the model file from assets
    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        AssetFileDescriptor fileDescriptor = assetManager.openFd("goals_recommendation.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Method to make predictions using the TensorFlow Lite model
    public float[] predictGoals(float[] inputData) {
        // Prepare input data buffer (adjust size based on input shape)
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(4 * inputData.length);  // 4 bytes for float32
        inputBuffer.order(ByteOrder.nativeOrder());
        for (float value : inputData) {
            inputBuffer.putFloat(value);
        }

        // Prepare output buffer (adjust size based on output shape)
        float[][] outputData = new float[1][3];  // Assuming 3 outputs: step goal, calorie goal, water intake goal

        // Run inference
        interpreter.run(inputBuffer, outputData);

        // Return predictions
        return outputData[0];
    }

    // Close the interpreter when done
    public void close() {
        interpreter.close();
    }
}
