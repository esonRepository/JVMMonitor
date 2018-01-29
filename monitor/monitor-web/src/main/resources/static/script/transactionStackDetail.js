$(function () {
    // 查询调用链某个节点详情
    $(".js-stack-nodes").bind("click", function (e) {
        var traceId = $(e.target).attr("data-transaction-trance-id");
        var name = $(e.target).attr("data-transaction-name");
        var callChainDepth = $(e.target).attr("data-transaction-depth");

        var data = {"name": name, "traceId": traceId, "callChainDepth": callChainDepth};
        $.post('/callChain/transactionStack.html', data, function (dataHtml) {
            $("#transactionStackModalContent").html(dataHtml);
            $("#transactionStackModal").modal('show');
        });
    });

    $(".js-close-call-chain-modal").bind("click", function (e) {
        $("#callChainDetailModal").modal('hide');
    });
});