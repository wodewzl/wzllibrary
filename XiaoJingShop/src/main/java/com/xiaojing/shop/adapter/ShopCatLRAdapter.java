package com.xiaojing.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.CheckBox;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.OneShopActivity;
import com.xiaojing.shop.activity.ShopListActivity;
import com.xiaojing.shop.activity.WebViewActivity;
import com.xiaojing.shop.mode.ShopCatVO;
import com.xiaojing.shop.view.NumberButton;

import java.util.List;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.xiaojing.shop.R.id.type5_same1;
//import ren.qinc.numberbutton.NumberButton;

/**
 * Created by Administrator on 2017/2/13.
 */

public class ShopCatLRAdapter extends BaseTurboAdapter<ShopCatVO, BaseViewHolder> {
    private OnMoneyChageListener moneyChageListener;

    public interface OnMoneyChageListener {
        void moneyChage();

        void allCheck(boolean isCheck);

        void cartDelete();
    }

    public ShopCatLRAdapter(Context context) {
        super(context);
    }

    public ShopCatLRAdapter(Context context, List<ShopCatVO> data) {
        super(context, data);
    }


    @Override
    protected int getDefItemViewType(int position) {
        ShopCatVO vo = getItem(position);
        if (vo.getStore_name() != null) {
            return 1;
        } else if (vo.getCart_id() != null) {
            return 2;
        } else if (vo.getNc_distinct() != null) {
            return 4;
        } else if ("没有商品".equals(vo.getGoods_name())) {
            return 5;
        } else {
            return 3;
        }

    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new HeadHolder(inflateItemView(R.layout.shop_cat_adapter_type1, parent));
        } else if (viewType == 2) {
            return new ViewHolder(inflateItemView(R.layout.shop_cat_adapter_type2, parent));
        } else if (viewType == 3) {
            return new HeadHolder2(inflateItemView(R.layout.shop_cat_adapter_type3, parent));
        } else if (viewType == 4) {
            return new ViewHolder2(inflateItemView(R.layout.shop_cat_adapter_type4, parent));
        } else {
            return new HeadHolder3(inflateItemView(R.layout.shop_cat_adapter_type5, parent));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void convert(BaseViewHolder holder, final ShopCatVO shopCatVO) {
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.nameTv.setText(shopCatVO.getGoods_name());
            viewHolder.priceTv.setText("￥" + shopCatVO.getGoods_price());
            viewHolder.numberBt.setmOnTextChangeListener(new NumberButton.OnTextChangeListener() {
                @Override
                public void onTextChange(int count) {
                    shopCatVO.setGoods_num(count + "");
                    moneyChageListener.moneyChage();
                }
            });
            viewHolder.numberBt.setBuyMax(99).setCurrentNumber(BaseCommonUtils.parseInt(shopCatVO.getGoods_num()));
            Picasso.with(mContext).load(shopCatVO.getGoods_image_url()).placeholder(R.drawable.img_default).into(viewHolder.shopImg);

            if (shopCatVO.isCheck()) {
                viewHolder.checkBox.setChecked(true);
            } else {
                viewHolder.checkBox.setChecked(false);
            }

//            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    boolean isCheck=false;//是否是点击选中的
//                    if(shopCatVO.isCheck()==b){
//                        isCheck=false;
//                    }else{
//                        isCheck=true;
//                    }
//                    shopCatVO.setCheck(b);
//                    ShopCatVO parentVO = null;
//                    for (int i = 0; i < mData.size(); i++) {
//                        if (shopCatVO.getStore_id().equals(mData.get(i).getStore_id()) && mData.get(i).getStore_name() != null) {
//                            parentVO = mData.get(i);
//                            break;
//                        }
//                    }
//
//                    for (int i = 0; i < mData.size(); i++) {
//                        if (shopCatVO.getStore_id().equals(mData.get(i).getStore_id()) && mData.get(i).getStore_name() == null) {
//                            if (mData.get(i).isCheck()) {
//                                parentVO.setCheck(true);
//                            } else {
//                                parentVO.setCheck(false);
//                                break;
//                            }
//                        }
//                    }
//
//                    if(isCheck){
//                        viewHolder.checkBox.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                notifyDataSetChanged();
//                            }
//                        }, 100);
//                    }
//
//                }
//            });

            viewHolder.checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean b = false;
                    if (shopCatVO.isCheck()) {
                        shopCatVO.setCheck(false);
                        viewHolder.checkBox.setChecked(false);
                    } else {
                        shopCatVO.setCheck(true);
                        viewHolder.checkBox.setChecked(true);
                    }

                    ShopCatVO parentVO = null;
                    for (int i = 0; i < mData.size(); i++) {
                        if (shopCatVO.getStore_id().equals(mData.get(i).getStore_id()) && mData.get(i).getStore_name() != null) {
                            parentVO = mData.get(i);
                            break;
                        }
                    }
                    for (int i = 0; i < mData.size(); i++) {
                        if (shopCatVO.getStore_id().equals(mData.get(i).getStore_id()) && mData.get(i).getStore_name() == null) {
                            if (mData.get(i).isCheck()) {
                                parentVO.setCheck(true);
                            } else {
                                parentVO.setCheck(false);
                                break;
                            }
                        }
                    }

                    viewHolder.checkBox.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    }, 100);
                }
            });

            viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定要删除吗?")
//                            .setContentText("删除成功")
                            .setConfirmText("确定")
                            .setCancelText("取消")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog.dismissWithAnimation();//直接消失
                                    mData.remove(shopCatVO);
                                    notifyDataSetChanged();
                                    moneyChageListener.cartDelete();
                                    sDialog.setTitleText("删除成功")
//                                            .setContentText("Your imaginary file has been deleted!")
                                            .setConfirmText("确定")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                                    sDialog.dismissWithAnimation();//直接消失
                                }
                            })
                            .show();
                }
            });

        } else if (holder instanceof ViewHolder2) {
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            Picasso.with(mContext).load(shopCatVO.getGoods_image_url()).into(viewHolder2.type5Img1);
            viewHolder2.type5Title1.setText(shopCatVO.getGoods_name());
            viewHolder2.type5Price1.setText("￥" + shopCatVO.getGoods_price());
            viewHolder2.type5Same1.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.C3_1, R.color.C1));
            viewHolder2.type5Same1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("gc_id", shopCatVO.getGc_id());
                    intent.setClass(mContext, ShopListActivity.class);
                    mContext.startActivity(intent);
                }
            });

            viewHolder2.shopLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("url", shopCatVO.getDetail_url());
                    intent.setClass(mContext, WebViewActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof HeadHolder) {
            final HeadHolder headHolder = (HeadHolder) holder;
            headHolder.nameTv.setText(shopCatVO.getStore_name());
//            headHolder.checkBox.setEnabled(false);
            if (shopCatVO.isCheck()) {
                headHolder.checkBox.setChecked(true);
            } else {
                headHolder.checkBox.setChecked(false);
            }

            headHolder.checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isCheck = false;
                    if (shopCatVO.isCheck()) {
                        isCheck = false;
                    } else {
                        isCheck = true;
                    }
                    shopCatVO.setCheck(isCheck);
                    headHolder.checkBox.setChecked(isCheck);
                    for (int i = 0; i < mData.size(); i++) {
                        if (shopCatVO.getStore_id().equals(mData.get(i).getStore_id()) && mData.get(i).getStore_name() == null) {
                            mData.get(i).setCheck(isCheck);
                        }
                    }
                    notifyDataSetChanged();
                }
            });

            headHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).getStore_name() != null) {
                            if (mData.get(i).isCheck()) {
                                moneyChageListener.allCheck(true);
                            } else {
                                moneyChageListener.allCheck(false);
                                break;
                            }
                        }
                    }
                }
            });

        } else if (holder instanceof HeadHolder3) {
            final HeadHolder3 headHolde3 = (HeadHolder3) holder;
            headHolde3.seeOtherTv.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.XJColor2, R.color.C1));
            headHolde3.seeOtherTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, OneShopActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }


    class ViewHolder extends BaseViewHolder {
        TextView nameTv, priceTv;
        ImageView shopImg;
        //        private ImageView checkImg;
        NumberButton numberBt;
        CheckBox checkBox;
        ImageView deleteImg;
        View checkView;

        public ViewHolder(View view) {
            super(view);
            nameTv = findViewById(R.id.name_tv);
            priceTv = findViewById(R.id.price_tv);
            shopImg = findViewById(R.id.shop_img);
//            checkImg = findViewById(R.id.check_img);
            numberBt = findViewById(R.id.number_bt);
            checkBox = findViewById(R.id.check_box);
            deleteImg = findViewById(R.id.delete_img);
            checkView = findViewById(R.id.view_check);
        }
    }

    class ViewHolder2 extends BaseViewHolder {
        TextView type5Title1, type5Price1, type5Same1;
        ImageView type5Img1;
        RelativeLayout shopLayout;

        public ViewHolder2(View view) {
            super(view);
            type5Title1 = findViewById(R.id.type5_title1);
            type5Price1 = findViewById(R.id.type5_price1);
            type5Img1 = findViewById(R.id.type5_img1);
            type5Same1 = findViewById(type5_same1);
            shopLayout = findViewById(R.id.shop_layout);
        }
    }

    class HeadHolder extends BaseViewHolder {
        private ImageView checkImg;
        private TextView nameTv;
        LinearLayout header;
        CheckBox checkBox;
        View checkView;

        public HeadHolder(View view) {
            super(view);
//            checkImg = findViewById(R.id.check_img);
            checkBox = findViewById(R.id.check_box);
            nameTv = findViewById(R.id.name_tv);
            header = findViewById(R.id.header);
            checkView = findViewById(R.id.view_check);

        }
    }

    class HeadHolder2 extends BaseViewHolder {

        public HeadHolder2(View view) {
            super(view);
//            header = findViewById(R.id.header);
        }
    }

    class HeadHolder3 extends BaseViewHolder {
        TextView seeOtherTv;

        public HeadHolder3(View view) {
            super(view);
            seeOtherTv = findViewById(R.id.see_other_tv);
        }
    }

    public OnMoneyChageListener getMoneyChageListener() {
        return moneyChageListener;
    }

    public void setMoneyChageListener(OnMoneyChageListener moneyChageListener) {
        this.moneyChageListener = moneyChageListener;
    }
}
