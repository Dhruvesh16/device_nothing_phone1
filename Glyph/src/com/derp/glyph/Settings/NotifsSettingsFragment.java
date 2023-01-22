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

package com.derp.glyph.Settings;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.android.internal.util.ArrayUtils;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

import java.util.List;

import com.derp.glyph.R;
import com.derp.glyph.Constants.Constants;
import com.derp.glyph.Manager.SettingsManager;
import com.derp.glyph.Utils.ServiceUtils;

public class NotifsSettingsFragment extends PreferenceFragmentCompat implements OnPreferenceChangeListener,
        OnMainSwitchChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.glyph_notifs_settings);

        PreferenceScreen mScreen = this.getPreferenceScreen();
        requireActivity().setTitle(R.string.glyph_settings_notifs_toggle_title);

        MainSwitchPreference mSwitchBar = (MainSwitchPreference) findPreference(Constants.GLYPH_NOTIFS_SUB_ENABLE);
        assert mSwitchBar != null;
        mSwitchBar.addOnSwitchChangeListener(this);
        mSwitchBar.setChecked(SettingsManager.isGlyphNotifsEnabled(requireActivity()));

        PreferenceCategory mCategory = (PreferenceCategory) findPreference(Constants.GLYPH_NOTIFS_SUB_CATEGORY);

        PackageManager mPackageManager = requireActivity().getPackageManager();
        List<ApplicationInfo> mApps = mPackageManager.getInstalledApplications(PackageManager.GET_GIDS);
        mApps.sort(new ApplicationInfo.DisplayNameComparator(mPackageManager));
        for (ApplicationInfo app : mApps) {
            if(mPackageManager.getLaunchIntentForPackage(app.packageName) != null  && !ArrayUtils.contains(Constants.APPSTOIGNORE, app.packageName)) { // apps with launcher intent
                SwitchPreference mSwitchPreference = new SwitchPreference(mScreen.getContext());
                mSwitchPreference.setKey(app.packageName);
                mSwitchPreference.setTitle(" " + app.loadLabel(mPackageManager).toString()); // add this space since the layout looks off otherwise
                mSwitchPreference.setIcon(app.loadIcon(mPackageManager));
                mSwitchPreference.setDefaultValue(true);
                mSwitchPreference.setOnPreferenceChangeListener(this);
                assert mCategory != null;
                mCategory.addPreference(mSwitchPreference);
            }
        }
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        //mHandler.post(() -> ServiceUtils.checkGlyphService(getActivity()));
        return true;
    }

    @Override
    public void onSwitchChanged(Switch switchView, boolean isChecked) {
        SettingsManager.setGlyphNotifsEnabled(requireActivity(), isChecked);
        ServiceUtils.checkGlyphService(getActivity());
    }

}
