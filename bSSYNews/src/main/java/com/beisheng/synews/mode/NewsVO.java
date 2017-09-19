package com.beisheng.synews.mode;

import java.io.Serializable;
import java.util.List;

public class NewsVO implements Serializable {
	private String code;
	private String retinfo;
	private String count;
	private String page;
	private String perpage;
	private String total;
	private List<NewsVO> slide;
	private String contentid;
	private String title;
	private String thumb;
	private String suburl;
	private List<NewsVO> list;
	private String creatime;
	private String comments;
	private String read;
	private String content;
	private String source;
	private String share_img;
	private String share_tit;
	private String share_des;
	private String share_url;
	private String pid;
	private String note;
	private String link;
	private List<NewsVO> plate;
	private int type = 0;// type为日报的悬浮类型
	private List<NewsVO> like;
	private NewsVO banner;
	private String thum;
	private List<NewsVO> reply;

	private String nickname;
	private String headpic;
	private String replyname;
	private String outurl;
	private String praise;
	private String ispraise;
	private String modelid;
	private String adv;
	private String userid;
	private String url;
	private String isReply;
	private String slider_time;
	private String slider_is;
	private String tags;
	private String subtitle;
	private String govermentid;
	private String goverment_title;
	private String goverment_contentid;
	private String typename;
	private List<NewsVO> children;
	private String parentid;
	private String replyNum;
	private String more_conmments;
	private String linkv1;
	private String introtitle;
	private String adv_type;
	private List<NewsVO> relatedlist;
	private int videoPosition = 0;

	private String created;
	private String titletype;
	private String read_content;

	public String getRead_content() {
		return read_content;
	}

	public void setRead_content(String read_content) {
		this.read_content = read_content;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRetinfo() {
		return retinfo;
	}

	public void setRetinfo(String retinfo) {
		this.retinfo = retinfo;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPerpage() {
		return perpage;
	}

	public void setPerpage(String perpage) {
		this.perpage = perpage;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<NewsVO> getSlide() {
		return slide;
	}

	public void setSlide(List<NewsVO> slide) {
		this.slide = slide;
	}

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getSuburl() {
		return suburl;
	}

	public void setSuburl(String suburl) {
		this.suburl = suburl;
	}

	public List<NewsVO> getList() {
		return list;
	}

	public void setList(List<NewsVO> list) {
		this.list = list;
	}

	public String getCreatime() {
		return creatime;
	}

	public void setCreatime(String creatime) {
		this.creatime = creatime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getShare_img() {
		return share_img;
	}

	public void setShare_img(String share_img) {
		this.share_img = share_img;
	}

	public String getShare_tit() {
		return share_tit;
	}

	public void setShare_tit(String share_tit) {
		this.share_tit = share_tit;
	}

	public String getShare_des() {
		return share_des;
	}

	public void setShare_des(String share_des) {
		this.share_des = share_des;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<NewsVO> getPlate() {
		return plate;
	}

	public void setPlate(List<NewsVO> plate) {
		this.plate = plate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<NewsVO> getLike() {
		return like;
	}

	public void setLike(List<NewsVO> like) {
		this.like = like;
	}

	public NewsVO getBanner() {
		return banner;
	}

	public void setBanner(NewsVO banner) {
		this.banner = banner;
	}

	public String getThum() {
		return thum;
	}

	public void setThum(String thum) {
		this.thum = thum;
	}

	public List<NewsVO> getReply() {
		return reply;
	}

	public void setReply(List<NewsVO> reply) {
		this.reply = reply;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadpic() {
		return headpic;
	}

	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}

	public String getReplyname() {
		return replyname;
	}

	public void setReplyname(String replyname) {
		this.replyname = replyname;
	}

	public String getOuturl() {
		return outurl;
	}

	public void setOuturl(String outurl) {
		this.outurl = outurl;
	}

	public String getPraise() {
		return praise;
	}

	public void setPraise(String praise) {
		this.praise = praise;
	}

	public String getIspraise() {
		return ispraise;
	}

	public void setIspraise(String ispraise) {
		this.ispraise = ispraise;
	}

	public String getModelid() {
		return modelid;
	}

	public void setModelid(String modelid) {
		this.modelid = modelid;
	}

	public String getAdv() {
		return adv;
	}

	public void setAdv(String adv) {
		this.adv = adv;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIsReply() {
		return isReply;
	}

	public void setIsReply(String isReply) {
		this.isReply = isReply;
	}

	public String getSlider_time() {
		return slider_time;
	}

	public void setSlider_time(String slider_time) {
		this.slider_time = slider_time;
	}

	public String getSlider_is() {
		return slider_is;
	}

	public void setSlider_is(String slider_is) {
		this.slider_is = slider_is;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getGovermentid() {
		return govermentid;
	}

	public void setGovermentid(String govermentid) {
		this.govermentid = govermentid;
	}

	public String getGoverment_title() {
		return goverment_title;
	}

	public void setGoverment_title(String goverment_title) {
		this.goverment_title = goverment_title;
	}

	public String getGoverment_contentid() {
		return goverment_contentid;
	}

	public void setGoverment_contentid(String goverment_contentid) {
		this.goverment_contentid = goverment_contentid;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public List<NewsVO> getChildren() {
		return children;
	}

	public void setChildren(List<NewsVO> children) {
		this.children = children;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(String replyNum) {
		this.replyNum = replyNum;
	}

	public String getMore_conmments() {
		return more_conmments;
	}

	public void setMore_conmments(String more_conmments) {
		this.more_conmments = more_conmments;
	}

	public String getLinkv1() {
		return linkv1;
	}

	public void setLinkv1(String linkv1) {
		this.linkv1 = linkv1;
	}

	public String getIntrotitle() {
		return introtitle;
	}

	public void setIntrotitle(String introtitle) {
		this.introtitle = introtitle;
	}

	public String getAdv_type() {
		return adv_type;
	}

	public void setAdv_type(String adv_type) {
		this.adv_type = adv_type;
	}

	public List<NewsVO> getRelatedlist() {
		return relatedlist;
	}

	public void setRelatedlist(List<NewsVO> relatedlist) {
		this.relatedlist = relatedlist;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getTitletype() {
		return titletype;
	}

	public void setTitletype(String titletype) {
		this.titletype = titletype;
	}

	@Override
	public String toString() {
		return "NewsVO{" + "code='" + code + '\'' + ", retinfo='" + retinfo + '\'' + ", count='" + count + '\'' + ", page='" + page + '\'' + ", perpage='" + perpage + '\'' + ", total='" + total
				+ '\'' + ", slide=" + slide + ", contentid='" + contentid + '\'' + ", title='" + title + '\'' + ", thumb='" + thumb + '\'' + ", suburl='" + suburl + '\'' + ", list=" + list
				+ ", creatime='" + creatime + '\'' + ", comments='" + comments + '\'' + ", read='" + read + '\'' + ", content='" + content + '\'' + ", source='" + source + '\'' + ", share_img='"
				+ share_img + '\'' + ", share_tit='" + share_tit + '\'' + ", share_des='" + share_des + '\'' + ", share_url='" + share_url + '\'' + ", pid='" + pid + '\'' + ", note='" + note + '\''
				+ ", link='" + link + '\'' + ", plate=" + plate + ", type=" + type + ", like=" + like + ", banner=" + banner + ", thum='" + thum + '\'' + ", reply=" + reply + ", nickname='"
				+ nickname + '\'' + ", headpic='" + headpic + '\'' + ", replyname='" + replyname + '\'' + ", outurl='" + outurl + '\'' + ", praise='" + praise + '\'' + ", ispraise='" + ispraise
				+ '\'' + ", modelid='" + modelid + '\'' + ", adv='" + adv + '\'' + ", userid='" + userid + '\'' + ", url='" + url + '\'' + ", isReply='" + isReply + '\'' + ", slider_time='"
				+ slider_time + '\'' + ", slider_is='" + slider_is + '\'' + ", tags='" + tags + '\'' + ", subtitle='" + subtitle + '\'' + ", govermentid='" + govermentid + '\''
				+ ", goverment_title='" + goverment_title + '\'' + ", goverment_contentid='" + goverment_contentid + '\'' + ", typename='" + typename + '\'' + ", children=" + children
				+ ", parentid='" + parentid + '\'' + ", replyNum='" + replyNum + '\'' + ", more_conmments='" + more_conmments + '\'' + ", linkv1='" + linkv1 + '\'' + ", introtitle='" + introtitle
				+ '\'' + ", adv_type='" + adv_type + '\'' + ", relatedlist=" + relatedlist + ", created='" + created + '\'' + ", titletype='" + titletype + '\'' + ", read_content='" + read_content
				+ '\'' + '}';
	}

	public int getVideoPosition() {
		return videoPosition;
	}

	public void setVideoPosition(int videoPosition) {
		this.videoPosition = videoPosition;
	}

}
