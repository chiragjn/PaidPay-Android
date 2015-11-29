/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.shoshin.paidpay;

/**
 * This custom object is used to populate the list adapter. It contains a reference
 * to an image, title, and the extra text to be displayed. Furthermore, it keeps track
 * of the current state (collapsed/expanded) of the corresponding item in the list,
 * as well as store the height of the cell in its collapsed state.
 */
public class ExpandableListItem implements OnSizeChangedListener {

    String card_name;
    String balance;
    boolean mIsExpanded;
    int card_logo;
    int mCollapsedHeight;
    int mExpandedHeight;
    String cardType;

    public ExpandableListItem(String card_name, int card_logo,int collaspedHeight, String balance,String type) {
    	this.card_name =  card_name;
        this.card_logo = card_logo;
        this.mCollapsedHeight = collaspedHeight;
        this.mIsExpanded = false;
        this.balance = balance;
        this.mExpandedHeight = -1;
        this.cardType = type;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean isExpanded) {
       mIsExpanded = isExpanded;
    }

    public String getCardName() {
        return card_name;
    }

    public int getCardLogo() {
        return card_logo;
    }

    public int getCollapsedHeight() {
        return mCollapsedHeight;
    }

    public void setCollapsedHeight(int collapsedHeight) {
        mCollapsedHeight = collapsedHeight;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String text) {
        this.balance = text;
    }

    public int getExpandedHeight() {
        return mExpandedHeight;
    }
    
    public String getType() {
        return cardType;
    }

    public void setExpandedHeight(int expandedHeight) {
        mExpandedHeight = expandedHeight;
    }

    @Override
    public void onSizeChanged(int newHeight) {
        setExpandedHeight(newHeight);
    }
}
