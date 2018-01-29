$(function () {

    var loadingHtml =
        "<div class='col-xs-12 text-center'>"
        + "<img src='/static/images/callChain/loading.gif'/> 数据加载中..."
        + "</div>";

    /**
     * 组件时间初始化
     */
    function initDateComponent() {

        // 初始化日期控件
        $("#startTime").datetimepicker({
            language: "zh-CN",
            autoclose: true,
            todayBtn: true,
            format: "yyyy-mm-dd hh:02:00",
            startView: 2,
            minView: 1
        }).on('changeDate', function (selected) {
            var minDate = new Date(selected.date.valueOf());
            $('#endTime').datetimepicker('setStartDate', minDate);
        });

        $("#endTime").datetimepicker({
            language: "zh-CN",
            autoclose: true,
            todayBtn: true,
            format: "yyyy-mm-dd hh:02:00",
            startView: 2,
            minView: 1
        }).on('changeDate', function (selected) {
            var maxDate = new Date(selected.date.valueOf());
            $('#startTime').datetimepicker('setEndDate', maxDate);
        });
    }


    /**
     * 查询调用链信息(提交调用链查询条件区域表单)
     *
     */
    function analyseDependency() {

        $("#dependency-waiting-content").html(loadingHtml);
        $.post(
            "/dependency/analyseDependency.html",
            $("#dependencySearchForm").serialize(),
            function (result) {
                $("#dependency-waiting-content").html('');
                var jsonResult = $.parseJSON(result);
                console.log(JSON.stringify(jsonResult['nodes']));
                console.log(JSON.stringify(jsonResult['links']));
                var option = {
                    title: {
                        text: ''
                    },
                    tooltip: {},
                    animationDurationUpdate: 1500,
                    animationEasingUpdate: 'quinticInOut',
                    series: [
                        {
                            type: 'graph',
                            layout: 'force',
                            symbolSize: 60,
                            roam: true,
                            label: {
                                normal: {
                                    show: true,
                                    position: 'top'
                                }
                            },
                            edgeSymbol: ['circle', 'arrow'],
                            edgeSymbolSize: [4, 10],
                            edgeLabel: {
                                normal: {
                                    textStyle: {
                                        fontSize: 20
                                    }
                                }
                            },
                            data: jsonResult['nodes'],
                            force: {
                                edgeLength: 50,
                                repulsion: 500,
                                gravity: 0.1
                            },
                            links: jsonResult['links']

                        }
                    ]
                };
                console.log(option);
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }
        );
    }

    // 时间控件
    initDateComponent();

    var myChart = echarts.init(document.getElementById("dependency-result"));

    //加载依赖关系
    analyseDependency();

    // 查询依赖关系
    $(".js-analyse-dependency").on("click", function () {
        analyseDependency();
    });

});