package xyz.migoo.authen;

/**
* @author xiaomi
 */
public final class Base64 {

    private static final int BASE_LENGTH = 128;
    private static final int LOOK_UP_LENGTH = 64;
    private static final int TWENTY_FOUR_BIT_GROUP = 24;
    private static final int EIGHT_BIT = 8;
    private static final int SIXTEEN_BIT = 16;
    private static final int FOUR_BIT = 4;
    private static final int SIGN = -128;
    private static final char PAD = '=';
    private static final boolean F_DEBUG = false;
    private static final byte[] BASE_64_ALPHABET = new byte[BASE_LENGTH];
    private static final char[] LOOK_UP_BASE_64_ALPHABET = new char[LOOK_UP_LENGTH];
    private static final char A = 'A';
    private static final char Z = 'Z';
    private static final char a = 'a';
    private static final char z = 'z';
    private static final char ZERO = '0';
    private static final char NINE = '9';
    private static final int TWENTY_FIVE = 25;
    private static final int TWENTY_SIX = 26;
    private static final int FIFTY_ONE = 51;
    private static final int FIFTY_TWO = 52;
    private static final int SIXTY_ONE = 61;
    private static final byte OXF = 0xf;
    private static final byte OX3 = 0x3;

    static {
        for (int i = 0; i < BASE_LENGTH; ++i) {
            BASE_64_ALPHABET[i] = -1;
        }
        for (int i = Z; i >= A; i--){
            BASE_64_ALPHABET[i] = (byte) (i - 'A');
        }
        for (int i = z; i >= a; i--){
            BASE_64_ALPHABET[i] = (byte) (i - 'a' + 26);
        }

        for (int i = NINE; i >= ZERO; i--){
            BASE_64_ALPHABET[i] = (byte) (i - '0' + 52);
        }

        BASE_64_ALPHABET['+'] = 62;
        BASE_64_ALPHABET['/'] = 63;

        for (int i = 0; i <= TWENTY_FIVE; i++) {
            LOOK_UP_BASE_64_ALPHABET[i] = (char) (A + i);
        }

        for (int i = TWENTY_SIX, j = 0; i <= FIFTY_ONE; i++, j++) {
            LOOK_UP_BASE_64_ALPHABET[i] = (char) (a + j);
        }

        for (int i = FIFTY_TWO, j = 0; i <= SIXTY_ONE; i++, j++) {
            LOOK_UP_BASE_64_ALPHABET[i] = (char) (ZERO + j);
        }
        LOOK_UP_BASE_64_ALPHABET[62] = '+';
        LOOK_UP_BASE_64_ALPHABET[63] = '/';

    }

    private static boolean isWhiteSpace(char octet) {
        return (octet == 0x20 || octet == 0xd || octet == 0xa || octet == 0x9);
    }

    private static boolean isPad(char octet) {
        return (octet == PAD);
    }

    private static boolean isData(char octet) {
        return (octet < BASE_LENGTH && BASE_64_ALPHABET[octet] != -1);
    }

    /**
     * Encodes hex octets into Base64
     *
     * @param binaryData Array containing binaryData
     * @return Encoded Base64 array
     */
    public static String encode(byte[] binaryData) {

        if (binaryData == null) {
            return null;
        }

        int lengthDataBits = binaryData.length * EIGHT_BIT;
        if (lengthDataBits == 0) {
            return "";
        }

        int fewerThan24bits = lengthDataBits % TWENTY_FOUR_BIT_GROUP;
        int numberTriplets = lengthDataBits / TWENTY_FOUR_BIT_GROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1
                : numberTriplets;
        char[] encodedData = new char[numberQuartet * 4];

        byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

        int encodedIndex = 0;
        int dataIndex = 0;
        if (F_DEBUG) {
            System.out.println("number of triplets = " + numberTriplets);
        }

        for (int i = 0; i < numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];

            if (F_DEBUG) {
                System.out.println("b1= " + b1 + ", b2= " + b2 + ", b3= " + b3);
            }

            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
                    : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
                    : (byte) ((b2) >> 4 ^ 0xf0);
            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6)
                    : (byte) ((b3) >> 6 ^ 0xfc);

            if (F_DEBUG) {
                System.out.println("val2 = " + val2);
                System.out.println("k4   = " + (k << 4));
                System.out.println("vak  = " + (val2 | (k << 4)));
            }

            encodedData[encodedIndex++] = LOOK_UP_BASE_64_ALPHABET[val1];
            encodedData[encodedIndex++] = LOOK_UP_BASE_64_ALPHABET[val2 | (k << 4)];
            encodedData[encodedIndex++] = LOOK_UP_BASE_64_ALPHABET[(l << 2) | val3];
            encodedData[encodedIndex++] = LOOK_UP_BASE_64_ALPHABET[b3 & 0x3f];
        }

        // form integral number of 6-bit groups
        if (fewerThan24bits == EIGHT_BIT) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x03);
            if (F_DEBUG) {
                System.out.println("b1=" + b1);
                System.out.println("b1<<2 = " + (b1 >> 2));
            }
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
                    : (byte) ((b1) >> 2 ^ 0xc0);
            encodedData[encodedIndex++] = LOOK_UP_BASE_64_ALPHABET[val1];
            encodedData[encodedIndex++] = LOOK_UP_BASE_64_ALPHABET[k << 4];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
        } else if (fewerThan24bits == SIXTEEN_BIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
                    : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
                    : (byte) ((b2) >> 4 ^ 0xf0);

            encodedData[encodedIndex++] = LOOK_UP_BASE_64_ALPHABET[val1];
            encodedData[encodedIndex++] = LOOK_UP_BASE_64_ALPHABET[val2 | (k << 4)];
            encodedData[encodedIndex++] = LOOK_UP_BASE_64_ALPHABET[l << 2];
            encodedData[encodedIndex++] = PAD;
        }

        return new String(encodedData);
    }

    /**
     * Decodes Base64 data into octects
     *
     * @param encoded string containing Base64 data
     * @return Array containind decoded data.
     */
    public static byte[] decode(String encoded) {

        if (encoded == null) {
            return null;
        }

        char[] base64Data = encoded.toCharArray();
        // remove white spaces
        int len = removeWhiteSpace(base64Data);

        if (len % FOUR_BIT != 0) {
            // should be divisible by four
            return null;
        }

        int numberQuadruple = (len / FOUR_BIT);

        if (numberQuadruple == 0) {
            return new byte[0];
        }

        byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
        char d1 = 0, d2 = 0, d3 = 0, d4 = 0;

        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        byte[] decodedData = new byte[(numberQuadruple) * 3];

        for (; i < numberQuadruple - 1; i++) {
            // if found "no data" just return null
            if (!isData((d1 = base64Data[dataIndex++]))
                    || !isData((d2 = base64Data[dataIndex++]))
                    || !isData((d3 = base64Data[dataIndex++]))
                    || !isData((d4 = base64Data[dataIndex++]))) {
                return null;
            }

            b1 = BASE_64_ALPHABET[d1];
            b2 = BASE_64_ALPHABET[d2];
            b3 = BASE_64_ALPHABET[d3];
            b4 = BASE_64_ALPHABET[d4];

            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }

        if (!isData((d1 = base64Data[dataIndex++]))
                || !isData((d2 = base64Data[dataIndex++]))) {
            // if found "no data" just return null
            return null;
        }

        b1 = BASE_64_ALPHABET[d1];
        b2 = BASE_64_ALPHABET[d2];

        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        // Check if they are PAD characters
        if (!isData((d3)) || !isData((d4))) {
            if (isPad(d3) && isPad(d4)) {
                // last 4 bits should be zero
                if ((b2 & OXF) != 0) {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            } else if (!isPad(d3) && isPad(d4)) {
                b3 = BASE_64_ALPHABET[d3];
                // last 2 bits should be zero
                if ((b3 & OX3 ) != 0) {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            } else {
                return null;
            }
        } else {
            // No PAD union.g 3cQl
            b3 = BASE_64_ALPHABET[d3];
            b4 = BASE_64_ALPHABET[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);

        }

        return decodedData;
    }

    /**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     *
     * @param data the byte array of base64 data (with WS)
     * @return the new length
     */
    private static int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        }

        // count characters that's not whitespace
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                data[newSize++] = data[i];
            }
        }
        return newSize;
    }
}