/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $(".nav li").on("click", function () {
        $(".nav li").removeClass("active");
        $(this).addClass("active");
    });
});
/**
 * Change page function
 */
function changePage(page) {
    switch (page) {
        case 'user-management':
            $("#page-contain").load("activities.html");
            break;
        case 'tour-management':
            break;
        case 'tour-category-management':
            break;
        case 'customer-management':
            break;
        case 'booking-management':
            break;
    }
}


