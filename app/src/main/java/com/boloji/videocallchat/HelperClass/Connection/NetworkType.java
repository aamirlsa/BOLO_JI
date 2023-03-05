package com.boloji.videocallchat.HelperClass.Connection;

public enum NetworkType {
    ACCEPTED("accepted"),
    REJECTED("rejected");
    
    private String id;

    private NetworkType(String str) {
        this.id = str;
    }

    public static NetworkType getType(String str) {
        NetworkType[] values = values();
        for (NetworkType typeResponse : values) {
            if (str.equals(typeResponse.getId())) {
                return typeResponse;
            }
        }
        return REJECTED;
    }

    public String getId() {
        return this.id;
    }
}
