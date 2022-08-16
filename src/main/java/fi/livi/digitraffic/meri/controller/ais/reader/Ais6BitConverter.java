/**
 * -----
 * Copyright (C) 2018 Digia
 * -----
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * 2019.02.14: Original work is used here as an base implementation
 */
package fi.livi.digitraffic.meri.controller.ais.reader;

public class Ais6BitConverter {
    public static String to6Bit(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {

            int charNbr = str.charAt(i) - 48;
            if (charNbr > 40) {
                charNbr -= 8;
            }

            sb.append(String.format("%6s", Integer.toBinaryString(charNbr)).replace(' ', '0'));
        }
        return sb.toString();
    }

    public static String to6BitEncodedString(String binaryStr) {
        int numberOfChars = binaryStr.length() / 6;
        int start = 0;

        char[] charArray = new char[numberOfChars];
        for (int i = 0; i < numberOfChars; i++) {
            charArray[i] = getAsciiCharFor6BitByte(binaryStr.substring(start, start + 6));
            start += 6;
        }
        return new String(charArray);
    }

    private static char getAsciiCharFor6BitByte(String binary6BitByte) {
        int ret = Integer.parseInt(binary6BitByte, 2);
        if (ret < 32) {
            ret += 64;
        }
        return (char) ret;
    }
}
