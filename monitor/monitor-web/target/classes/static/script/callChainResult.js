$(function () {
    // 查询调用链详情
    $(".js-call-chain-detail").bind("click", function (e) {
        var name=$(e.target).attr("data-call-chain-name");
        var data = {"name":name};
        $.post('/callChain/callDetail.html',data,function(dataHtml){
            $("#callChainDetailModalContent").html(dataHtml);
            $("#callChainDetailModal").modal('show');
        });
    });
});