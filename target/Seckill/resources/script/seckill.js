var seckill = {
    // 封装秒杀相关ajax的url地址
    URL: {
        now: function () {
            return "/seckill/time/now";
        },
        exposer: function (seckillId) {
            return "/seckill/" + seckillId + "/exposer";
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    handleSeckill : function (seckillId, node) {
        // 获取秒杀地址，控制显示逻辑，执行秒杀
        // 先将原来的空白处替换成开始秒杀按钮
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>')
        // ajax暴露秒杀请求地址
        $.post(
            seckill.URL.exposer(seckillId),
            {},
            function (result) {
                if (result && result['success']) {
                    // 获取暴露地址时返回的数据部分
                    var exposer = result['data'];
                    // 判断暴露地址的结果，如果在秒杀时间段，就为开始秒杀按钮附加上一次点击事件
                    if (exposer['exposed']) {
                        // 开启秒杀
                        // 获取秒杀地址
                        var md5 = exposer['md5'];
                        var killUrl = seckill.URL.execution(seckillId, md5);
                        // 只绑定一次点击事件
                        $('#killBtn').one('click', function () {
                            // 执行秒杀请求
                            // 1、先禁用按钮
                            $(this).addClass('disabled');
                            // 2、发送秒杀请求
                            $.post(
                                killUrl,
                                {},
                                function (result) {
                                    var killResult = result['data'];
                                    var stateInfo = killResult['stateInfo'];
                                    if (result && result['success']) {
                                        node.html('<span class="label label-success">' + stateInfo + '</span>')
                                    } else {
                                        node.html('<span class="label label-default">' + stateInfo + '</span>')
                                    }
                                }
                            );
                        });
                        node.show();
                    } else {
                        // 有可能客户端与服务端事件不同步，导致未开启，就重新进入倒计时
                        var now = exposer['now'];
                        var start = exposer['start'];
                        var end = exposer['end'];
                        seckill.countdown(seckillId, now, start, end);
                    }
                } else {
                    // 控制台打印暴露地址时候出现的异常
                    console.log('result: ' + result)
                }
            }
        );
    },
    // 检验手机号
    validdatePhone : function(phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    // 设置倒计时界面
    countdown : function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        // 时间判断
        if (nowTime > endTime) {
            // 秒杀结束
            seckillBox.html('秒杀结束!');
        } else if (nowTime < startTime) {
            // 秒杀未开始，计时事件绑定
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
                // 时间完成后回调事件
            }).on('finish.countdown', function () {
                // 开始秒杀
                seckill.handleSeckill(seckillId, seckillBox);
            })
        } else {
            // 秒杀开始
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },
    // 详情页秒杀逻辑
    detail: {
        // 详情页初始化
        init : function (params) {
            // 手机验证登录，计时交互
            // 在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            // 验证手机号
            if (!seckill.validdatePhone(killPhone)) {
                // 绑定phone
                var killPhoneModal = $('#killPhoneModal');
                // 显示输入电话号的模态框
                killPhoneModal.show();

                // 提交电话号码时进行校验
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validdatePhone(inputPhone)) {
                        // 电话写入cookie
                        $.cookie('killPhone', inputPhone, {expires:7, path:'/seckill'});
                        // 刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
                    }
                });
            }
            // 已经登录
            // 计时交互
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endtTime = params['endTime'];
            $.get(
                seckill.URL.now(),
                {},
                function (result) {
                    if (result && result['success']) {
                        var nowTime = result['data'];
                        // 时间判断
                        seckill.countdown(seckillId, nowTime, startTime, endtTime)
                    } else {
                        console.log('result: ' + result);
                    }
                }
            );
        }
    }
}