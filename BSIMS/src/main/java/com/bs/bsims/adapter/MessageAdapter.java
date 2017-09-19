
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.bs.bsims.R;
import com.bs.bsims.activity.EXTSharedfilesdGroupHomeActivity;
import com.bs.bsims.activity.JournalSerachActivity;
import com.bs.bsims.activity.MessageListActivity;
import com.bs.bsims.activity.ScheduleTrendActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chat.ChatMessageHomeActivity;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.MainVO;
import com.bs.bsims.onekey.remove.DropCover.OnDragCompeteListener;
import com.bs.bsims.onekey.remove.WaterDrop;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSPointImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageAdapter extends BaseAdapter implements OnDragCompeteListener {

    public List<MainVO> list;
    private boolean mFlag = false;// 一键消除标志

    private Context mContext;
    private MainVO mCurrentDragVO;
    public SwipeMenuListView listView;

    public interface DragCallback {
        // status 代表回调执行的事件 0为拖拽红点消除 2为删除 3为置顶
        public void callback(int status, String type);

    }

    private DragCallback mDragCallback;

    public MessageAdapter(Context context, DragCallback dragCallback) {
        this.mContext = context;
        this.mDragCallback = dragCallback;
        list = new ArrayList<MainVO>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        MainVO vo = list.get(position);
        if (Integer.parseInt(vo.getType()) == 11 || Integer.parseInt(vo.getType()) == 12 || Integer.parseInt(vo.getType()) == 19 || Integer.parseInt(vo.getType()) == 20) {
            // 只有一个菜单
            if ("1".equals(vo.getIstop())) {
                return 2;// 已经被置顶;
            } else {
                return 1;// 未被置顶
            }
        } else {
            // 有二个菜单
            if ("1".equals(vo.getIstop())) {
                return 4;// 已经被置顶;
            } else {
                return 3;// 未被置顶
            }
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.general_home_itme1, null);
            holder.mItemImg = (BSPointImageView) convertView.findViewById(R.id.item_icon);
            holder.mItemTitleTv = (TextView) convertView.findViewById(R.id.item_title);
            holder.mItemDepictTv = (TextView) convertView.findViewById(R.id.item_depict);
            holder.mItemWaterDrop = (WaterDrop) convertView.findViewById(R.id.item_water_drop);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.item_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MainVO vo = list.get(position);
        final Intent intent = new Intent();

        if ("0".equals(vo.getCount())) {
            holder.mItemWaterDrop.setVisibility(View.INVISIBLE);
            holder.mItemWaterDrop.setText("0");
        } else {
            holder.mItemWaterDrop.setVisibility(View.VISIBLE);
            holder.mItemWaterDrop.setText(CommonUtils.isNormalCount(vo.getCount()));
        }
        holder.mItemWaterDrop.setOnDragCompeteListener(this);
        holder.mItemWaterDrop.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent arg1) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        String type = vo.getType();
        holder.mItemWaterDrop.setTag(type);
        holder.mItemTitleTv.setText(vo.getTypename());

        intent.putExtra("type", type);
        intent.setClass(mContext, MessageListActivity.class);
        intent.putExtra("name", vo.getTypename());
        intent.putExtra("relatedCount", vo.getRelatedCount());
        intent.putExtra("allCount", vo.getAllCount());

        holder.mTimeTv.setText(DateUtils.parseMDHM(vo.getTime()));
        if (CommonUtils.isNormalString(vo.getTitle()))
            holder.mItemDepictTv.setText(vo.getTitle());
        Class<com.bs.bsims.R.drawable> cls = R.drawable.class;
        int imageId;
        switch (Integer.parseInt(type)) {
            case 1:
                // 通知
                try {
                    imageId = cls.getDeclaredField("office003").getInt(null);
                    holder.mItemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra("noticeid", "");
                intent.putExtra("sortid", "3");
                intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                intent.putExtra("isshow", "1");
                break;
            case 2:
                // 公文
                try {
                    imageId = cls.getDeclaredField("office004").getInt(null);
                    holder.mItemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra("noticeid", "");
                intent.putExtra("sortid", "11");
                intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                intent.putExtra("isshow", "1");
                break;
            case 3:
                // 制度
                try {
                    imageId = cls.getDeclaredField("office005").getInt(null);
                    holder.mItemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra("noticeid", "");
                intent.putExtra("sortid", "12");
                intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                break;
            case 7:
                // 审批事务
                holder.mItemImg.setImageResource(R.drawable.affair001);
                intent.putExtra("isall", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL001));
                intent.putExtra("modeid", "0");
                intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                break;
            case 8:
                // 任务跟进
                holder.mItemImg.setImageResource(R.drawable.affair002);
                intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                break;
            case 9:
                // 企业风采
                try {
                    imageId = cls.getDeclaredField("office006").getInt(null);
                    holder.mItemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra("noticeid", "");
                intent.putExtra("sortid", "19");
                intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                intent.putExtra("isshow", "1");
                break;
            case 10:
                // 意见分享
                holder.mItemImg.setImageResource(R.drawable.affair003);
                intent.putExtra("isboss", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL003));
                intent.putExtra("isall", "1");
                intent.putExtra("isadd", "0");
                break;

            case 11:
                // 日程管理
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_general_09);
                intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                intent.setClass(mContext, ScheduleTrendActivity.class);
                break;

            case 12:
                // 企业云盘
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_general_10);
                intent.putExtra("key", "1");
                intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                intent.setClass(mContext, EXTSharedfilesdGroupHomeActivity.class);
                break;
            case 13:
                // 销售机会
                holder.mItemImg.setImageResource(R.drawable.marketing005);
                break;
            case 14:
                // 合同
                holder.mItemImg.setImageResource(R.drawable.home_crm_traded);
                break;
            case 15:
                // 客户
                holder.mItemImg.setImageResource(R.drawable.marketing004);
                if (!"0".equals(vo.getCount())) {
                    intent.putExtra("unread", "1");
                }

                break;
            case 16:
                // 联系人列表
                holder.mItemImg.setImageResource(R.drawable.home_crm_contacts);
                break;
            case 17:
                // 回款审批
                holder.mItemImg.setImageResource(R.drawable.home_crm_repayment);
                break;

            case 18:
                // 跟单记录
                holder.mItemImg.setImageResource(R.drawable.home_crm_visitor);
                if (CommonUtils.isNormalString(vo.getTitle()))
                    holder.mItemDepictTv.setText(vo.getTitle());
                break;

            case 19:
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_general_02);
                if (CommonUtils.isNormalString(vo.getTitle()))
                    holder.mItemDepictTv.setText(vo.getTitle());

                intent.putExtra("isremind", "1");
                intent.putExtra("defferent_goin", "2");
                intent.setClass(mContext, JournalSerachActivity.class);
                break;
            // IM聊天
            case 20:
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_im);
                intent.setClass(mContext, ChatMessageHomeActivity.class);
                break;

            default:
                break;
        }
        vo.setIntent(intent);
        return convertView;
    }

    public void setFlag(boolean flag) {
        this.mFlag = flag;
    }

    public void updateData(List<MainVO> list) {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();

    }

    public void sortList() {
        Comparator comp = new Comparator() {
            public int compare(Object o1, Object o2) {
                int one = 0;
                int two = 0;
                MainVO p1 = (MainVO) o1;
                MainVO p2 = (MainVO) o2;
                one = Integer.parseInt(p1.getTime());
                two = Integer.parseInt(p2.getTime());
                if (one < two) {
                    return 1;
                } else if (one == two) {
                    return 0;
                } else {
                    return -1;
                }
            }
        };
        Collections.sort(list, comp);
    }

    static class ViewHolder {
        private BSPointImageView mItemImg;
        private WaterDrop mItemWaterDrop;
        private TextView mItemTitleTv, mItemDepictTv, mTimeTv;
        private LinearLayout mItemLayout;
        private HorizontalScrollView scrollView;
    }

    @Override
    public void onDrag(View v) {
        mDragCallback.callback(0, v.getTag().toString());
    }

    public SwipeMenuListView getListView() {
        return listView;
    }

    public void setListView(SwipeMenuListView listView) {
        this.listView = listView;
        initListViewMenu();
    }

    public void initListViewMenu() {
        listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                MainVO vo = list.get(position);
                switch (index) {
                    case 0:
                        if ("1".equals(vo.getIstop())) {
                            sortList();
                            notifyDataSetChanged();
                            vo.setIstop("0");
                            menu.getMenuItem(index).setTitle("置顶");
                            mDragCallback.callback(4, vo.getType());
                        } else {
                            list.remove(position);
                            final List<MainVO> newList = new ArrayList<MainVO>();
                            newList.add(vo);
                            newList.addAll(list);
                            updateData(newList);
                            vo.setIstop("1");
                            menu.getMenuItem(index).setTitle("取消置顶");
                            mDragCallback.callback(3, vo.getType());
                        }
                        break;
                    case 1:
                        list.remove(position);
                        notifyDataSetChanged();
                        mDragCallback.callback(2, vo.getType());
                        break;
                }
            }
        });
        listView.setMenuCreator(creator);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (arg3 != -1) {
                    MainVO vo = list.get((int) arg3);
                    if (vo.getIntent() != null)
                        mContext.startActivity(vo.getIntent());

                }
            }
        });
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            createMenu(menu, menu.getViewType());
        }

        private void createMenu(SwipeMenu menu, int status) {
            SwipeMenuItem item1 = new SwipeMenuItem(mContext);
            item1.setWidth(CommonUtils.dip2px(mContext, 90));
            item1.setBackground(new ColorDrawable(mContext.getResources().getColor(R.color.C6)));
            item1.setTitleSize(14);
            item1.setTitleColor(Color.WHITE);

            SwipeMenuItem item2 = new SwipeMenuItem(mContext);
            item2.setWidth(CommonUtils.dip2px(mContext, 90));
            item2.setBackground(new ColorDrawable(mContext.getResources().getColor(R.color.C9)));
            item2.setTitle("删除");
            item2.setTitleSize(14);
            item2.setTitleColor(Color.WHITE);

            switch (status) {
            // 只有一个菜单为被置顶的
                case 1:
                    item1.setTitle("置顶");
                    menu.addMenuItem(item1);
                    break;
                // 只有一个菜单被置顶的
                case 2:
                    item1.setTitle("取消置顶");
                    menu.addMenuItem(item1);
                    break;
                // 有二个菜单为被置顶的
                case 3:
                    item1.setTitle("置顶");
                    menu.addMenuItem(item1);
                    menu.addMenuItem(item2);
                    break;
                // 有二个菜单为置顶的
                case 4:
                    item1.setTitle("取消置顶");
                    menu.addMenuItem(item1);
                    menu.addMenuItem(item2);
                    break;

                default:
                    break;
            }
        }

    };

}
