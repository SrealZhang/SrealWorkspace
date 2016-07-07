/*
 * Copyright (c) 2015, 张涛.
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
package com.app.sample.chatting.bean;

/**
 * emoji表情的javabean
 *
 * @author kymjs (http://www.kymjs.com/) on 6/8/15.
 */
public class Emojicon {
    private String name; //在网络传递中的值
    private byte[] code; //在系统中所代表的值
    private String value; //code转换为String的值

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return code转换为String的值
     */
    public String getValue() {
        if (code == null) {
            return null;
        } else {
            return new String(code);
        }
    }
}