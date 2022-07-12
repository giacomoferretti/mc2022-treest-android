package com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api;

public class SetUserRequest extends SidRequest {
    private final String name;
    private final String picture;

    private SetUserRequest(Builder builder) {
        super(builder.getSid());
        this.name = builder.name;
        this.picture = builder.picture;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public static class Builder extends SidRequest {
        private String name;
        private String picture;

        public Builder(String sid) {
            super(sid);
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPicture(String picture) {
            this.picture = picture;
            return this;
        }

        public SetUserRequest build() /*throws Exception*/ {
            SetUserRequest request = new SetUserRequest(this);

            /*int count = 0;
            Field[] allFields = SetUserRequest.class.getDeclaredFields();
            for (Field field : allFields) {
                field.setAccessible(true);
                try {
                    Object o = field.get(request);
                    if (o != null) {
                        count++;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            if (count < 1) {
                throw new Exception("Minimum number of set fields (1) not reached");
            }*/

            // Check if at least name or picture were provided
            /*if (request.getName() == null && request.getPicture() == null) {
                throw new Exception("You need to provide at least one of the following parameters: name, picture");
            }*/

            return request;
        }
    }
}
