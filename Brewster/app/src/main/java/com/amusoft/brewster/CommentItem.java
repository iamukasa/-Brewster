package com.amusoft.brewster;

/**
 * Created by irving on 8/7/15.
 */
public class CommentItem {
    String sComment;
    String sCommentor;
    public CommentItem(String scomment,String scommentor){
        this.sComment=scomment;
        this.sCommentor=scommentor;
    }

    public String getsComment() {
        return sComment;
    }

    public String getsCommentor() {
        return sCommentor;
    }

    public void setsComment(String sComment) {
        this.sComment = sComment;
    }

    public void setsCommentor(String sCommentor) {
        this.sCommentor = sCommentor;
    }
}
