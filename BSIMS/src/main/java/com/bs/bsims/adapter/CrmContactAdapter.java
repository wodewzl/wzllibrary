
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.ContactDepTabResultVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrmContactAdapter extends BaseAdapter {
    public List<ContactDepTabResultVO> list = null;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private static BSDialog mDialog;
    private String mNoHeadColors[] = {
            "#7A929E", "#6194FF", "#65BEE6", "#F75E8C", "#39C3B4", "#FD953C", "#9B89B9",
    };
    private int colorIndex = 0;

    public CrmContactAdapter(Context context, List<ContactDepTabResultVO> list) {
        this.mContext = context;
        this.list = list;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        final ContactDepTabResultVO pVo = list.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_contact_item_adapter, null);
            holder.mContastTv = (TextView) convertView.findViewById(R.id.contacts_tv);
            holder.mPostionTv = (TextView) convertView.findViewById(R.id.postion_tv);
            holder.mCompanyTv = (TextView) convertView.findViewById(R.id.company_tv);
            holder.mImgMessage = (ImageView) convertView.findViewById(R.id.img_message);
            holder.mImgPhone = (ImageView) convertView.findViewById(R.id.img_phone);
            holder.mItemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            holder.mHeadIcon = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            holder.sex = (ImageView) convertView.findViewById(R.id.sex);
            holder.mText_item = (TextView) convertView.findViewById(R.id.text_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(pVo.getSortLetters());
        } else {
            holder.tvLetter.setVisibility(View.GONE);
        }

        mImageLoader.displayImage(pVo.getLheadpic(), holder.mHeadIcon, mOptions);
        holder.mHeadIcon.setUserId(pVo.getUserid());//HL:获取头像对应的用户ID，以便响应跳转
        holder.mContastTv.setText(pVo.getLname());

        if ("1".equals(pVo.getIsread())) {
            holder.mHeadIcon.setIsread("1");
        } else {
            holder.mHeadIcon.setIsread("0");
        }
        if (pVo.getPost().equals("暂无")) {
            holder.mPostionTv.setVisibility(View.GONE);
        } else {
            holder.mPostionTv.setVisibility(View.VISIBLE);
            holder.mPostionTv.setText(pVo.getPost());
        }

        if (pVo.getSex().equals("暂无")) {
            holder.sex.setVisibility(View.GONE);
        } else if (pVo.getSex().equals("男")) {
            holder.sex.setVisibility(View.VISIBLE);
            holder.sex.setImageResource(R.drawable.sex_man);
        } else if (pVo.getSex().equals("女")) {
            holder.sex.setVisibility(View.VISIBLE);
            holder.sex.setImageResource(R.drawable.sex_woman);
        }

        holder.mImgMessage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (pVo.getPhone() == null) {
                    CustomToast.showShortToast(mContext, "没有手机号，不能发短信");
                } else {
                    initSimpleListDialog(mContext, "请发短信", pVo.getPhone(), "0");
                }
            }
        });

        // 注意要把pVo.getPhone()， pVo.getTel()整合在一起
        holder.mImgPhone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (pVo.getPhone() == null && pVo.getTel() == null) {
                    CustomToast.showShortToast(mContext, "没有号码，不能打电话");
                } else {
                    int len;
                    String[] addStr = null;
                    if (pVo.getPhone() == null) {
                        len = pVo.getTel().length;
                        addStr = new String[len];
                        addStr = pVo.getTel();
                    } else if (pVo.getTel() == null) {
                        len = pVo.getPhone().length;
                        addStr = new String[len];
                        addStr = pVo.getPhone();
                    } else if (pVo.getPhone().length != 0 && pVo.getTel().length != 0) {
                        len = pVo.getPhone().length + pVo.getTel().length;
                        addStr = new String[len];
                        int len1 = 0;
                        for (int in = 0; in < len; in++) {
                            if (in < pVo.getPhone().length) {
                                addStr[in] = pVo.getPhone()[in];
                            }
                            if (in >= pVo.getPhone().length) {
                                addStr[in] = pVo.getTel()[len1];
                                len1++;
                            }
                        }
                    }

                    initSimpleListDialog(mContext, "请拨打电话", addStr, "1");
                }

            }
        });
        if (pVo.getLheadpic() != null && !pVo.getLheadpic().equals("")) {
            holder.mText_item.setVisibility(View.GONE);
            holder.mHeadIcon.setVisibility(View.VISIBLE);
            mImageLoader.displayImage(pVo.getLheadpic(),  holder.mHeadIcon, mOptions);
        }
        else {
            holder.mText_item.setVisibility(View.VISIBLE);
            holder.mHeadIcon.setVisibility(View.GONE);
            holder.mText_item.setText(pVo.getNickname());
            holder.mText_item.setBackgroundDrawable(CommonUtils.setBackgroundShap(mContext, 40, "#ffffff", mNoHeadColors[colorIndex]));
            colorIndex++;
            colorIndex = colorIndex < 6 ? colorIndex : 0;
        }
        holder.mCompanyTv.setText(pVo.getCompany());
        return convertView;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    static class ViewHolder {
        // 联系人布局
        private TextView mContastTv, mPostionTv, mCompanyTv;
        private ImageView mImgMessage, mImgPhone;
        private LinearLayout mItemLayout, mPictureLayout;
        private BSCircleImageView mHeadIcon;
        private ImageView sex;
        private TextView tvLetter,mText_item;

    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * 
     * @param list
     */
    public void updateListView(List<ContactDepTabResultVO> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    // 简单的弹出对话框封装，拨打电话
    public void initSimpleListDialog(Context context, String title, final String[] array, final String type) {

        final List<Map<String, Object>> list = getListItem(array);
        // dropdown_one_leve_item
        SimpleAdapter adapter = new SimpleAdapter(context, list, R.layout.dropdown_one_leve_item,
                new String[] {
                        "option"
                }, new int[] {
                        R.id.textview
                });
        ListView listView = new ListView(context);
        listView.setAdapter(adapter);
        // listView.setDivider(null);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // textview.setText(array[(int) arg3]);
                // textview.setTag(arg3 + "");
                if (type.equals("1")) {
                    CommonUtils.call(mContext, array[arg2]);
                } else if (type.equals("0")) {
                    CommonUtils.sendMsg(mContext, array[arg2]);
                }
                mDialog.dismiss();
            }
        });
        mDialog = new BSDialog(context, title, listView, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            }
        });

        mDialog.show();
        mDialog.setButtonVisible(false);
    }

    public List<Map<String, Object>> getListItem(String[] array) {
        String[] tmpArray = array;
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array.length; i++) {
            Map<String, Object> listem = new HashMap<String, Object>();
            listem.put("option", array[i]);
            listems.add(listem);
        }
        return listems;
    }

}
