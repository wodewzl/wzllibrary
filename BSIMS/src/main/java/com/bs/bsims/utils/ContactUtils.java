
package com.bs.bsims.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactUtils {
    private static final String TAG = "ContactList";

    // 获取系统数据库联系人Phone表字段信息
    private static final String[] phoneContact = new String[] {
            Phone.CONTACT_ID, Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID
    };
    // 联系人的ID
    private static final int contactID_Contact = 0;
    // 联系人名称
    private static final int name_Contact = 1;
    // 电话号码
    private static final int num_Contact = 2;
    // 头像ID
    private static final int phoneID_Contact = 3;

    /**
     * 获取手机联系人信息:只获取正确非重复手机号
     * 
     * @param context
     * @return ArrayList
     */
    public static ArrayList<ContactInfo> getContactsList(Context context) {

        ContentResolver resolver = context.getContentResolver();
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, phoneContact,
                null, null, ContactsContract.Contacts.DISPLAY_NAME
                        + " COLLATE LOCALIZED ASC");

        // <name,ContactInfo>
        HashMap<String, ContactInfo> nameMap = new HashMap<String, ContactInfo>();
        // 通讯录的所有手机号码
        ArrayList<String> phoneList = new ArrayList<String>();
        // 通讯录的所有座机号
        ArrayList<String> telList = new ArrayList<String>();

        if (phoneCursor != null) {
            // 通讯录所有联系人信息
            ArrayList<ContactInfo> contacts = new ArrayList<ContactInfo>();

            ContactInfo contactInfo = null;
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(num_Contact);

                // 得到联系人名称
                String contactName = phoneCursor.getString(name_Contact);
                // 得到联系人ID
                int contactid = phoneCursor.getInt(contactID_Contact);
                // 得到联系人头像ID
                Long photoid = phoneCursor.getLong(phoneID_Contact);

                // 当手机号码为空的 跳过此次循环
                if (TextUtils.isEmpty(phoneNumber)) {
                    continue;
                }

                // 得到手机号(号码不为空并且正确手机号)
                if (!TextUtils.isEmpty(phoneNumber)
                        && RegexUtils.isMoblieNo(phoneNumber)) {

                    if (phoneList.contains(phoneNumber)) {
                        continue;
                    } else {
                        phoneList.add(phoneNumber);
                        if (!nameMap.containsKey(contactName)) {
                            contactInfo = new ContactInfo();
                            contactInfo.name = contactName;
                            contactInfo.mobilePhoneList.add(phoneNumber);
                            contacts.add(contactInfo);
                            nameMap.put(contactName, contactInfo);
                        } else {
                            contactInfo = nameMap.get(contactName);
                            contactInfo.mobilePhoneList.add(phoneNumber);
                        }
                    }
                }

                // 得到座机号(号码不为空并且非正确手机号)
                if (!TextUtils.isEmpty(phoneNumber)
                        && !RegexUtils.isMoblieNo(phoneNumber)) {

                    if (telList.contains(phoneNumber)) {
                        continue;
                    } else {
                        telList.add(phoneNumber);
                        if (!nameMap.containsKey(contactName)) {
                            contactInfo = new ContactInfo();
                            contactInfo.name = contactName;
                            contactInfo.mobileTelList.add(phoneNumber);
                            contacts.add(contactInfo);
                            nameMap.put(contactName, contactInfo);
                        } else {
                            contactInfo = nameMap.get(contactName);
                            contactInfo.mobileTelList.add(phoneNumber);
                        }
                    }
                }

            }
            phoneCursor.close();
            return getNewData(contacts);
        }
        return null;
    }

    // 读取通讯录的全部的联系人
    // 需要先在raw_contact表中遍历id，并根据id到data表中获取数据
    // @SuppressWarnings("unused")
    @SuppressWarnings("unused")
    public static ArrayList<ContactInfo> testReadAll(Context context) {
        HashMap<String, ContactInfo> nameMap = new HashMap<String, ContactInfo>();
        // 通讯录的所有手机号码
        ArrayList<String> phoneList = new ArrayList<String>();
        // 通讯录的所有座机号
        ArrayList<String> telList = new ArrayList<String>();
        // 通讯录的所以邮箱
        ArrayList<String> emailList = new ArrayList<String>();

        // uri = content://com.android.contacts/contacts
        Uri uri = Uri.parse("content://com.android.contacts/contacts"); // 访问raw_contacts表
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[] {
                Data._ID
        }, null, null, null); // 获得_id属性

        if (cursor != null) {
            // 通讯录所有联系人信息
            ArrayList<ContactInfo> contacts = new ArrayList<ContactInfo>();
            ContactInfo contactInfo = null;

            while (cursor.moveToNext()) {
                StringBuilder buf = new StringBuilder();
                int id = cursor.getInt(0);// 获得id并且在data中寻找数据
                buf.append("id=" + id);
                uri = Uri.parse("content://com.android.contacts/contacts/" + id + "/data"); // 如果要获得data表中某个id对应的数据，则URI为content://com.android.contacts/contacts/#/data
                Cursor cursor2 = resolver.query(uri, new String[] {
                        Data.DATA1, Data.MIMETYPE
                }, null, null, null); // data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
                String contactName = null;
                ArrayList<String> phoneNumberList = new ArrayList<String>();
                ArrayList<String> contactEmailList = new ArrayList<String>();
                String contactAddress = null;
                String contactOrga = null;
                while (cursor2.moveToNext()) {
                    String data = cursor2.getString(cursor2.getColumnIndex("data1"));
                    if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")) { // 如果是名字
                        contactName = data;
                    }
                    else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/phone_v2")) { // 如果是电话
                        phoneNumberList.add(data);
                    }
                    // else if
                    // (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/email_v2"))
                    // { // 如果是email
                    // contactEmailList.add(data);
                    // }
                    // else if
                    // (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/postal-address_v2"))
                    // { // 如果是地址
                    // contactAddress = data;
                    // }
                    else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/organization")) { // 如果是公司
                        contactOrga = data;
                    }
                }

                // 当名字为空的 跳过此次循环
                if (TextUtils.isEmpty(contactName)) {
                    continue;
                }

                // 当手机号码为空的 跳过此次循环
                if (phoneNumberList == null) {
                    continue;
                } else {
                    // 对手机
                    for (int i = 0; i < phoneNumberList.size(); i++) {
                        String phoneNumber = phoneNumberList.get(i);
                        // 得到手机号(号码不为空并且正确手机号)
                        if (!TextUtils.isEmpty(phoneNumber)
                                && RegexUtils.isMoblieNo(phoneNumber)) {

                            if (phoneList.contains(phoneNumber)) {
                                continue;
                            } else {
                                phoneList.add(phoneNumber);
                                if (!nameMap.containsKey(contactName)) {
                                    contactInfo = new ContactInfo();
                                    contactInfo.name = contactName;
                                    contactInfo.mobilePhoneList.add(phoneNumber);
                                    contacts.add(contactInfo);
                                    nameMap.put(contactName, contactInfo);
                                } else {
                                    contactInfo = nameMap.get(contactName);
                                    contactInfo.mobilePhoneList.add(phoneNumber);
                                }
                            }
                        }

                        // 得到座机号(号码不为空并且非正确手机号)
                        if (!TextUtils.isEmpty(phoneNumber)
                                && !RegexUtils.isMoblieNo(phoneNumber)) {

                            if (telList.contains(phoneNumber)) {
                                continue;
                            } else {
                                telList.add(phoneNumber);
                                if (!nameMap.containsKey(contactName)) {
                                    contactInfo = new ContactInfo();
                                    contactInfo.name = contactName;
                                    contactInfo.mobileTelList.add(phoneNumber);
                                    contacts.add(contactInfo);
                                    nameMap.put(contactName, contactInfo);
                                } else {
                                    contactInfo = nameMap.get(contactName);
                                    contactInfo.mobileTelList.add(phoneNumber);
                                }
                            }
                        }
                    }
                }

                // 得到邮箱(这里有问题，报空指针)
                // if (contactEmailList.size() != 0) {
                // contactInfo.contactEmailList.addAll(contactEmailList);
                // }

                // contactInfo.address = contactAddress;
                contactInfo.organization = contactOrga;

                // }
                // String str = buf.toString();
            }
            cursor.close();
            return getNewData(contacts);

        }
        return null;
    }

    // 去掉只有电话号码、没名字的数据；这种情况名字、手机号一样
    public static ArrayList<ContactInfo> getNewData(ArrayList<ContactInfo> infoList) {
        ArrayList<ContactInfo> cList = new ArrayList<ContactInfo>();
        for (int i = 0; i < infoList.size(); i++) {
            ContactInfo info = infoList.get(i);
            if (info.getPhoneNumList() != null && info.getPhoneNumList().size() >= 1) {
                if (info.getName().equals(info.getPhoneNumList().get(0))) {
                    infoList.remove(info);
                }
            }
            if (info.getMobileTelList() != null && info.getMobileTelList().size() >= 1) {
                if (info.getName().equals(info.getMobileTelList().get(0))) {
                    infoList.remove(info);
                }
            }
        }
        cList.addAll(infoList);
        return cList;

    }

    /**
     * 联系人信息类
     */
    public static class ContactInfo implements Serializable {
        public String name; // 联系人姓名
        public ArrayList<String> mobilePhoneList; // 手机号码
        public ArrayList<String> mobileTelList; // 座机号码
        public ArrayList<String> contactEmailList;// 邮箱
        public String address;// 住址
        public String organization;// 组织或公司单位
        public String sortLetters;
        public Boolean isSelect;

        public ContactInfo() {
            CheckNullPointer();
        }

        public ArrayList<String> getMobileTelList() {
            return mobileTelList;
        }

        public void setMobileTelList(ArrayList<String> mobileTelList) {
            this.mobileTelList = mobileTelList;
        }

        /**
         * 避免发生空指针异常
         */
        public void CheckNullPointer() {
            if (mobilePhoneList == null)
                mobilePhoneList = new ArrayList<String>();

            if (mobileTelList == null)
                mobileTelList = new ArrayList<String>();

            if (name == null)
                name = "";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<String> getPhoneNumList() {
            return mobilePhoneList;
        }

        public void setPhoneNumList(ArrayList<String> mobilePhone) {
            mobilePhoneList = mobilePhone;
        }

        public String getSortLetters() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        public Boolean getIsSelect() {
            return isSelect;
        }

        public void setIsSelect(Boolean isSelect) {
            this.isSelect = isSelect;
        }

    }

    public static class RegexUtils {
        /**
         * 验证是否是有效手机号 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 　　联通：130、131、132、152、155、156、185、186 　　电信：133、153、180、189、（1349卫通）
         * 
         * @param mobiles
         * @return
         */
        public static boolean isMoblieNo(String mobiles) {
            mobiles = mobiles.replaceAll(" ", "");
            if (mobiles.length() != 11) {
                if (mobiles.contains("+86")) {
                    int a = mobiles.length();
                    int b = "+86".length();
                    mobiles = mobiles.substring(b, a);
                }
            }

            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,2,3,5-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        }
    }

}
