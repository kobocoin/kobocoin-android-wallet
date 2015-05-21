/*
 * Copyright 2011-2014 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.bushstar.kobocoin_android_wallet.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bushstar.kobocoinj.core.Address;
import com.bushstar.kobocoinj.uri.KobocoinURI;

import com.bushstar.kobocoin_android_wallet.Configuration;
import com.bushstar.kobocoin_android_wallet.Constants;
import com.bushstar.kobocoin_android_wallet.WalletApplication;
import com.bushstar.kobocoin_android_wallet.util.BitmapFragment;
import com.bushstar.kobocoin_android_wallet.util.Nfc;
import com.bushstar.kobocoin_android_wallet.util.Qr;
import com.bushstar.kobocoin_android_wallet.util.WalletUtils;
import com.bushstar.kobocoin_android_wallet.R;

/**
 * @author Andreas Schildbach
 */
public final class WalletAddressFragment extends Fragment
{
	private FragmentActivity activity;
	private WalletApplication application;
	private Configuration config;
	private NfcManager nfcManager;

	private View kobocoinAddressButton;
	private TextView kobocoinAddressLabel;
	private ImageView kobocoinAddressQrView;

	private Address lastSelectedAddress;

	private Bitmap qrCodeBitmap;

	@Override
	public void onAttach(final Activity activity)
	{
		super.onAttach(activity);

		this.activity = (FragmentActivity) activity;
		this.application = (WalletApplication) activity.getApplication();
		this.config = application.getConfiguration();
		this.nfcManager = (NfcManager) activity.getSystemService(Context.NFC_SERVICE);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.wallet_address_fragment, container, false);
		kobocoinAddressButton = view.findViewById(R.id.kobocoin_address_button);
		kobocoinAddressLabel = (TextView) view.findViewById(R.id.kobocoin_address_label);
		kobocoinAddressQrView = (ImageView) view.findViewById(R.id.kobocoin_address_qr);

		kobocoinAddressButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				AddressBookActivity.start(activity, false);
			}
		});

		kobocoinAddressQrView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				handleShowQRCode();
			}
		});

		return view;
	}

	@Override
	public void onResume()
	{
		super.onResume();

		config.registerOnSharedPreferenceChangeListener(prefsListener);

		updateView();
	}

	@Override
	public void onPause()
	{
		config.unregisterOnSharedPreferenceChangeListener(prefsListener);

		Nfc.unpublish(nfcManager, getActivity());

		super.onPause();
	}

	private void updateView()
	{
		final Address selectedAddress = application.determineSelectedAddress();

		if (!selectedAddress.equals(lastSelectedAddress))
		{
			lastSelectedAddress = selectedAddress;

			kobocoinAddressLabel.setText(WalletUtils.formatAddress(selectedAddress, Constants.ADDRESS_FORMAT_GROUP_SIZE,
					Constants.ADDRESS_FORMAT_LINE_SIZE));

			final String addressStr = KobocoinURI.convertToKobocoinURI(selectedAddress, null, null, null);

			final int size = (int) (256 * getResources().getDisplayMetrics().density);
			qrCodeBitmap = Qr.bitmap(addressStr, size);
			kobocoinAddressQrView.setImageBitmap(qrCodeBitmap);

			Nfc.publishUri(nfcManager, getActivity(), addressStr);
		}
	}

	private void handleShowQRCode()
	{
		BitmapFragment.show(getFragmentManager(), qrCodeBitmap);
	}

	private final OnSharedPreferenceChangeListener prefsListener = new OnSharedPreferenceChangeListener()
	{
		@Override
		public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key)
		{
			if (Configuration.PREFS_KEY_SELECTED_ADDRESS.equals(key))
				updateView();
		}
	};
}
