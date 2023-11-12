package com.jonlin.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 帖子视图
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class InterfaceInfoVO implements Serializable {

    /**
     * 用户名
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户名
     */
    private String description;

    /**
     * 用户名
     */
    private String url;

    /**
     * 用户名
     */
    private String requestHeader;

    /**
     * 用户名
     */
    private String responseHeader;

    /**
     * 用户名
     */
    private Integer status;

    /**
     * 用户名
     */
    private String method;

    /**
     * 用户名
     */
    private Long userId;

    /**
     * 用户名
     */
    private Date createTime;

    /**
     * 用户名
     */
    private Date updateTime;


    /**
     * 用户名
     */
    private Integer idDelete;

    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param postVO
     * @return
     */
//    public static Post voToObj(InterfaceInfoVO postVO) {
//        if (postVO == null) {
//            return null;
//        }
//        Post post = new Post();
//        BeanUtils.copyProperties(postVO, post);
//        List<String> tagList = postVO.getTagList();
//        if (tagList != null) {
//            post.setTags(GSON.toJson(tagList));
//        }
//        return post;
//    }
//
//    /**
//     * 对象转包装类
//     *
//     * @param post
//     * @return
//     */
//    public static InterfaceInfoVO objToVo(Post post) {
//        if (post == null) {
//            return null;
//        }
//        InterfaceInfoVO postVO = new InterfaceInfoVO();
//        BeanUtils.copyProperties(post, postVO);
//        postVO.setTagList(GSON.fromJson(post.getTags(), new TypeToken<List<String>>() {
//        }.getType()));
//        return postVO;
//    }
}
