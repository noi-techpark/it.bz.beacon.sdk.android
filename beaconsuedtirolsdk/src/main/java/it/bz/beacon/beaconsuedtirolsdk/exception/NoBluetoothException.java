// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package it.bz.beacon.beaconsuedtirolsdk.exception;

public class NoBluetoothException extends Exception {

    public NoBluetoothException() {
    }

    public NoBluetoothException(Throwable throwable) {
        super(throwable);
    }

    public NoBluetoothException(String message) {
        super(message);
    }
}
