package com.github.tdudziak.gps_lock_lock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter
{
    List<Item> mItems = new ArrayList<Item>();
    Context mContext;

    public static class Item {
        long id;
        String title;
        String titleCondensed;
        int imageResourceId;
    }

    public MenuAdapter(Context context, int menu_resource) {
        mContext = context;
        try {
            parseMenu(mContext.getResources().getXml(menu_resource));
        } catch(IOException ioex) {
            throw new RuntimeException(ioex);
        } catch(XmlPullParserException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).id;
    }

    public Item getItemById(long id) {
        for(Item it : mItems) {
            if(it.id == id) return it;
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = mItems.get(position);
        View result = null;

        if(convertView != null) {
            // try to reuse old view
            result = convertView;
        } else {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            result = li.inflate(R.layout.menu_item, parent, false);
        }

        // use title-description format if shorter variant supplied in titleCondensed
        if(item.titleCondensed != null && item.titleCondensed.length() > 0) {
            ((TextView) result.findViewById(R.id.menuItemTitleCondensed)).setText(item.titleCondensed);
            TextView textTitle = (TextView) result.findViewById(R.id.menuItemTitle);
            textTitle.setText(item.title);
            textTitle.setVisibility(View.VISIBLE);
        } else {
            ((TextView) result.findViewById(R.id.menuItemTitleCondensed)).setText(item.title);
            result.findViewById(R.id.menuItemTitle).setVisibility(View.GONE);
        }

        // show image if available
        ImageView image = (ImageView) result.findViewById(R.id.menuItemImage);
        if (item.imageResourceId != 0) {
            image.setImageResource(item.imageResourceId);
            image.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.GONE);
        }

        return result;
    }

    private boolean parserSkipTo(XmlPullParser parser, String tag_name)
            throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();

        while(eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                String name_got = parser.getName();

                if(name_got.equals(tag_name)) {
                    return true;
                } else {
                    throw new RuntimeException("Expected tag: " + tag_name + " but got " + name_got);
                }
            }
            eventType = parser.next();
        }

        return false;
    }

    private void parseMenu(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        final String XML_NAMESPACE = "http://schemas.android.com/apk/res/android";

        // skip to the first "menu" starting tag
        boolean cont = parserSkipTo(parser, "menu");
        parser.next();

        // iterate through all the items
        while(cont) {
            cont = parserSkipTo(parser, "item");
            if(cont) {
                AttributeSet attrs = Xml.asAttributeSet(parser);
                Item it = new Item();
                it.titleCondensed = attrs.getAttributeValue(XML_NAMESPACE, "titleCondensed");
                it.title = attrs.getAttributeValue(XML_NAMESPACE, "title");
                it.id = attrs.getAttributeResourceValue(XML_NAMESPACE, "id", 0);
                it.imageResourceId = attrs.getAttributeResourceValue(XML_NAMESPACE, "icon", 0);
                mItems.add(it);
            }
            parser.next();
        }
    }
}
