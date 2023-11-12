-- `interface_info`
create table if not exists `interface_info`
(
    `id` bigint not null auto_increment comment '用户名' primary key,
    `name` varchar(256) not null comment '用户名',
    `description` varchar(256) not null comment '用户名',
    `url` varchar(256) not null comment '用户名',
    `requestHeader` text not null comment '用户名',
    `responseHeader` text not null comment '用户名',
    `status` int not null comment '用户名',
    `method` varchar(256) not null comment '用户名',
    `userId` bigint not null comment '用户名',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP not null comment '用户名',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP not null comment '用户名',
    `idDelete` tinyint not null comment '用户名'
    ) comment '`interface_info`';

insert into `interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `idDelete`) values ('董智宸', '武文', 'www.margorie-quigley.co', '徐苑博', '姜子骞', 0, '贺睿渊', 685078, 0);
insert into `interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `idDelete`) values ('赵立辉', '夏煜祺', 'www.renae-koelpin.org', '万笑愚', '范文轩', 0, '雷潇然', 8950563, 0);
insert into `interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `idDelete`) values ('戴驰', '周笑愚', 'www.donn-berge.co', '沈聪健', '胡昊天', 0, '于懿轩', 64787577, 0);
insert into `interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `idDelete`) values ('贺天磊', '韦修杰', 'www.elwood-cruickshank.name', '姜鸿煊', '郑远航', 0, '蒋鸿涛', 542, 0);
insert into `interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `idDelete`) values ('宋梓晨', '谭思聪', 'www.karan-emard.org', '邱伟诚', '石智宸', 0, '史涛', 15523416, 0);
insert into `interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `idDelete`) values ('宋峻熙', '沈明轩', 'www.audra-langosh.co', '韦涛', '崔哲瀚', 0, '姜展鹏', 3, 0);
insert into `interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `idDelete`) values ('江琪', '孟志泽', 'www.kiyoko-purdy.net', '彭思源', '潘志强', 0, '谢雨泽', 67388, 0);
insert into `interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `idDelete`) values ('谢修洁', '邓瑾瑜', 'www.jimmy-frami.net', '韩乐驹', '熊博涛', 0, '李俊驰', 651437, 0);
insert into `interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `idDelete`) values ('洪越彬', '覃晟睿', 'www.barton-yost.io', '孙文轩', '孟鑫鹏', 0, '周文博', 721824108, 0);
insert into `interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `idDelete`) values ('袁笑愚', '范峻熙', 'www.hung-hickle.net', '夏博涛', '丁弘文', 0, '曹苑博', 90993397, 0);