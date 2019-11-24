package com.android.giveandtake.home;

public class Post {
    private int imageResocure;
    private String NameAsk;
    private String PhoneAsk;
    private String city;
    private String Give;
    private String Take;
    private String currentUserID;
    private String freeText;
    private String PostId;

    public void setNameAsk(String nameAsk) {
        NameAsk = nameAsk;
    }

    public void setPhoneAsk(String phoneAsk) {
        PhoneAsk = phoneAsk;
    }

    public void setFreeText(String newFreeText) {
        freeText = newFreeText;
    }


    public int getImageResocure() {
        return imageResocure;
    }

    public String getNameAsk() {
        return NameAsk;
    }

    public String getPhoneAsk() {
        return PhoneAsk;
    }
    public String getGive() {
        return Give;
    }
    public String getTake() {
        return Take;
    }
    public String getPostid() {
        return PostId;
    }
    public String getCity() {
        return city;
    }
    public String getcurrentUserID() {
        return currentUserID;
    }
    public String getfreeText() {
        return freeText;
    }



    public Post(int img ,String nameAsk, String phoneAsk,String City ,String GiveAsk, String TakeAsk,String moreInfoText ,String id, String postid) {
        imageResocure = img;
        NameAsk = nameAsk;
        PhoneAsk = phoneAsk;
        Give = GiveAsk;
        Take = TakeAsk;
        currentUserID = id;
        freeText = moreInfoText;
        PostId = postid;
        city = City;

    }
}