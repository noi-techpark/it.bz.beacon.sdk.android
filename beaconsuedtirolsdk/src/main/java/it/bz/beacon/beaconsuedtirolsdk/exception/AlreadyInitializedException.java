// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package it.bz.beacon.beaconsuedtirolsdk.exception;

public class AlreadyInitializedException extends RuntimeException {
    public AlreadyInitializedException() {
        super("The Beacon SDK has already been initialized.");
    }
}
