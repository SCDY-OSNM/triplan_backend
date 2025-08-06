package scdy.configservice.enums;

public enum ImageFormat {
    JPEG("jpeg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}),
    PNG("png", new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A}),
    GIF("gif", new byte[]{(byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38});

    private final String name;
    private final byte[] magicNumber;

    ImageFormat(String name, byte[] magicNumber) {
        this.name = name;
        this.magicNumber = magicNumber.clone(); // 방어적 복사
    }


    public byte[] getMagicNumber() {
        return magicNumber.clone(); // 방어적 복사로 불변성 보장
    }

    public int getMagicNumberLength() {
        return magicNumber.length;
    }

    /**
     * 파일 확장자로부터 ImageFormat을 찾는 유틸리티 메서드
     * @param extension 파일 확장자 (예: "jpg", "png")
     * @return 해당하는 ImageFormat, 없으면 null
     */
    public static ImageFormat fromExtension(String extension) {
        if (extension == null) {
            return null;
        }

        String lowerExt = extension.toLowerCase();
        switch (lowerExt) {
            case "jpg":
            case "jpeg":
                return JPEG;
            case "png":
                return PNG;
            case "gif":
                return GIF;
            default:
                return null;
        }
    }

    /**
     * MIME 타입으로부터 ImageFormat을 찾는 유틸리티 메서드
     * @param mimeType MIME 타입 (예: "image/jpeg")
     * @return 해당하는 ImageFormat, 없으면 null
     */
    public static ImageFormat fromMimeType(String mimeType) {
        if (mimeType == null) {
            return null;
        }

        String lowerMime = mimeType.toLowerCase();
        return switch (lowerMime) {
            case "image/jpeg" -> JPEG;
            case "image/png" -> PNG;
            case "image/gif" -> GIF;
            default -> null;
        };
    }

    /**
     * ImageFormat에 해당하는 MIME 타입 반환
     * @return MIME 타입 문자열
     */
    public String getMimeType() {
        return switch (this) {
            case JPEG -> "image/jpeg";
            case PNG -> "image/png";
            case GIF -> "image/gif";
            default -> null;
        };
    }
}