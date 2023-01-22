/*
 * Copyright (C) 2022 Paranoid Android
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

package com.derp.glyph.Utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.derp.glyph.Constants.Constants;

public final class FileUtils {

    private static final String TAG = "GlyphFileUtils";

    public static void writeLine(String fileName, String value) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(value);
        } catch (FileNotFoundException e) {
            Log.w(TAG, "No such file " + fileName + " for writing", e);
        } catch (IOException e) {
            Log.e(TAG, "Could not write to file " + fileName, e);
        }
    }

    public static void writeLine(String fileName, int value) {
        writeLine(fileName, Integer.toString(value));
    }

    public static void writeLine(String fileName, float value) {
        writeLine(fileName, Float.toString(value));
    }

    public static void writeSingleLed(int led, int value) {
        writeLine(Constants.SINGLELEDPATH, led + " " + value);
    }
}
