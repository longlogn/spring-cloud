package com.cxlsky.enums;

/**
 * @ClassName: FileType
 * @Description: 文件类型
 * @author: Dean
 * @date: 2018/8/23
 */
public enum FileTypeEnum {
    /** 图片文件 */
    IMAGE(1, "图片文件"),
    /** 音频文件 */
    AUDIO(2, "音频文件"),
    /** 文本文件 */
    TEXT(3, "文本文件");

    FileTypeEnum(int value, String meaning) {
        this.value = value;
        this.meaning = meaning;
    }

    private final int value;

    private final String meaning;

    public int getValue() {
        return this.value;
    }

    public String getMeaning() {
        return this.meaning;
    }

    public String getName() {
        return this.name();
    }

    public static FileTypeEnum valueOf(Integer value) {
        if (value == null) {
            return null;
        }
        for (FileTypeEnum e : FileTypeEnum.values()) {
            if (e.getValue() == value) {
                return e;
            }
        }
        return null;
    }
}
