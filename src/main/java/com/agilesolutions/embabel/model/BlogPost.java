package com.agilesolutions.embabel.model;

public sealed interface BlogPost permits DraftPost {
    String title();
    String content();
}