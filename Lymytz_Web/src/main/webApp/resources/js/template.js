/* 
 * Fonction utile pour le fonctionnement du template
 */
function displayHistorique() {
//    $('.historique').removeClass('display');

}

var node;
var height_zone;
var listVerify = null, unchecklist;

function definedTabView(node, unchecklist, height_zone) {
    definedTabView(node, unchecklist, height_zone, 4, 22);
}

function definedTabView(node, unchecklist, height_zone, nombre) {
    definedTabView(node, unchecklist, height_zone, nombre, 22);
}

function definedTabView(node, unchecklist, height_zone, nombre, heigth_start) {
    this.node = node;
    this.height_zone = height_zone;
    this.unchecklist = unchecklist;

    var tabview_top = $('#' + node).find('.tabview_top');
    var tabview_content = $('#' + node).find('.tabview_content');

    var count = tabview_content.children().length;
    var height = heigth_start;
    var index = 0;
    var line = 0;
    var reinitial_checkbox = 0;
    var reinitial_row = 0;

    tabview_top.find('.checkbox').append('<table><tr>');
    while (index < count) {
        var child = tabview_content.children().eq(index);
        var label = child.find('.block_slide_title').find('.slide_title').html();
        var checked = "checked";
        if (this.unchecklist !== null ? this.unchecklist !== '' : false) {
            var list = String(this.unchecklist).split(',');
            for (var i = 0; i < list.length; i++) {
                if (parseInt(index) === parseInt(list[i])) {
                    checked = "unchecked";
                    break;
                }
            }
        }
        tabview_top.find('.checkbox').append('<td><input ' + checked + ' id="checkbox_' + index + '" type="checkbox" onclick="onClikCheckBoxTabView(' + index + ', ' + line + ')"/> <span style="font-size: 12px;margin-right: 5px;margin-left: -5px">' + label.trim() + '</span></td>');
        index++;
        reinitial_row++;
        reinitial_checkbox++;

        if (reinitial_checkbox === nombre) {
            if (index < count) {
                tabview_top.find('.checkbox').append('</tr><tr>');
            }
            reinitial_checkbox = 0;
            height += 22;
        }
        if (reinitial_row === 2) {
            reinitial_row = 0;
            line++;
        }
    }
    tabview_top.find('.checkbox').append('</tr></table>');
    tabview_top.css("height", height);
    buildTabview(tabview_content);
    if (this.unchecklist !== null ? this.unchecklist !== '' : false) {
        var list = String(this.unchecklist).split(',');
        for (var i = 0; i < list.length; i++) {
            index = parseInt(list[i]);
            line = 0;
            if (index > 1) {
                line = parseInt(index / 2);
            }
            onClikCheckBoxTabView(index, line);
        }
    }
}

function buildTabview(tabview_content) {
    var count = tabview_content.children().length;
    var index = 0;
    var float = true;
    var line = 1;
    while (index < count) {
        var node = tabview_content.children().eq(index);
        node.css("height", this.height_zone);
        if (line === 1) {
            if (index >= count - 1) {
                node.css("clear", "both");
                node.css("width", "99.60%");
                float = false;
            }
            if (float) {
                node.css("float", "left");
                node.css("width", "49.25%");
                node.css("clear", "right");
            }
        } else {
            node.css("float", "right");
            node.css("width", "49.25%");
        }
        line++;
        if (line > 2) {
            line = 1;
        }
        index++;
    }
    var colonnes = parseInt(Math.round(count / 2));
    $('#' + this.node).data('hauteur', colonnes * parseFloat(this.height_zone));
}

function onClikCheckBoxTabView(index, line) {
    this.listVerify = null;
    var tabview_top = $('#' + this.node).find('.tabview_top');
    var tabview_content = $('#' + this.node).find('.tabview_content');
    var checkbox = tabview_top.find('.checkbox').find('#checkbox_' + index).prop('checked');
    var child = tabview_content.children().eq(index);
    child.css("display", (checkbox ? 'block' : 'none'));

    var count = tabview_content.children().length;
    var first = line * 2;
    var second = first + 1;
    var preview = null;
    if (first === index && second < count) {
        preview = tabview_content.children().eq(second);
    } else if (second === index && first < count) {
        preview = tabview_content.children().eq(first);
    }
    // Determination de la largeur
    if (preview !== null) {
        var display = preview.css("display");
        if (display !== "none") {
            preview.css("width", (checkbox ? "49.25%" : "99.60%"));
            if (checkbox) {
                child.css("width", "49.25%");
            }
        } else {
            if (checkbox) {
                child.css("width", "99.60%");
            }
        }
    }
    definedWithZoneTabView(index, line);
}

function definedWithZoneTabView(index, line) {
    if (this.listVerify !== null) {
        var list = String(this.listVerify).split(',');
        for (var i = 0; i < list.length; i++) {
            if (parseInt(index) === parseInt(list[i])) {
                return;
            }
        }
    }
    var height_content = $('body').find('.part_scroll').css("height");
    var tabview_top = $('#' + this.node).find('.tabview_top');
    var tabview_content = $('#' + this.node).find('.tabview_content');
    var checkbox = tabview_top.find('.checkbox').find('#checkbox_' + index).prop('checked');
    var child = tabview_content.children().eq(index);
    child.css("display", (checkbox ? 'block' : 'none'));

    var count = tabview_content.children().length;
    var first = line * 2;
    var second = first + 1;
    var preview = null, index_preview = null;
    if (first === index && second < count) {
        index_preview = second;
    } else if (second === index && first < count) {
        index_preview = first;
    }
    if (index_preview !== null) {
        preview = tabview_content.children().eq(index_preview);
    }
    // Determination de la hauteur
    var colonnes = Math.round(count / 2);
    var top = line - 1;
    var bottom = line + 1;
    var height_top = 0;
    var height_bottom = 0;
    if (top > -1) {
        first = top * 2;
        second = first + 1;
        var top_child1 = tabview_content.children().eq(first);
        var top_child2 = tabview_content.children().eq(second);
        if (top_child1.css("display") === "none" && top_child2.css("display") === "none") {
            height_top = parseFloat(this.height_zone);
//                height_top = parseFloat(top_child1.css("height").replace('px', ''));
        }
    }
    if (bottom < parseInt(colonnes) + 1) {
        first = bottom * 2;
        second = first + 1;
        var bottom_child1 = tabview_content.children().eq(first);
        var bottom_child2 = tabview_content.children().eq(second);
        if (bottom_child1.css("display") === "none" && (second < count ? bottom_child2.css("display") === "none" : true)) {
            height_bottom = parseFloat(this.height_zone);
//                height_bottom = parseFloat(bottom_child1.css("height").replace('px', ''));
        }
    }
    var height = (parseFloat(this.height_zone) + parseFloat(height_top) + parseFloat(height_bottom));
    if (parseFloat(height_content) > 0) {
        if (parseFloat(height_content) >= parseFloat(this.height_zone)) {
            first = 0;
            for (var i = 0; i < count; i++) {
                var current = tabview_content.children().eq(i);
                if (current.css("display") !== "none") {
                    first = i;
                    break;
                }
            }
            second = 0;
            while (first < count) {
                if (first !== index) {
                    var current = tabview_content.children().eq(first);
                    if (current.css("display") !== "none") {
                        second += parseFloat(this.height_zone);
                    } else {
                        if (first % 2 === 0) {
                            first -= 1;
                        } else {
                            first += 1;
                        }
                        current = tabview_content.children().eq(first);
                        if (current.css("display") !== "none") {
                            second += parseFloat(this.height_zone);
                        }
                    }
                }
                first += 2;
            }
            if ((parseFloat(second) + parseFloat(height)) > parseFloat(height_content) ? parseFloat(height) > parseFloat(this.height_zone) : false) {
                height -= parseFloat(this.height_zone);
            }
            if (parseFloat(height) > parseFloat(height_content)) {
                height = parseFloat(height_content);
            }
        }
    }
    child.css("height", height);
    if (preview !== null) {
        preview.css("height", height);
    }
    if (this.listVerify === null) {
        this.listVerify = index;
    } else {
        this.listVerify += "," + index;
    }
    if (index_preview !== null) {
        this.listVerify += "," + index_preview;
    }
    for (var i = 0; i < count; i++) {
        var current = tabview_content.children().eq(i);
        if (current.css("display") !== "none") {
            line = 0;
            if (i > 1) {
                line = parseInt(i / 2);
            }
            definedWithZoneTabView(i, line);
        }
    }
}


$(function() {
    $('.breadCumb').click(function() {
        if ($('.historique').hasClass('display')) {
            $('.historique').fadeOut(20);
            $('.historique').removeClass('display');
        } else {
            $('.historique').fadeIn(100);
            $('.historique').addClass('display');
        }
    });
});


/************************************************************/
function collapseForm(suffix) {
    collapsesForm('', suffix);
}
function collapsesForm(prefix, suffix) {
    $('.' + prefix + 'yvs_list_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_grid_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_form_' + suffix).fadeIn(1000);
    $('.' + prefix + 'yvs_att_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_tab_' + suffix).fadeOut(500);
    $('.' + prefix + 'gridNav').css('display', 'none');
    $('#' + prefix + 'update_' + suffix + ' , #' + prefix + 'delete_' + suffix + ' , #' + prefix + 'option_' + suffix).css('display', 'none');
    $('#' + prefix + 'search_' + suffix + ' , #' + prefix + 'find_' + suffix).css('display', 'none');
    $('#' + prefix + 'save_' + suffix + ', #' + prefix + 'cancel_' + suffix + ', #' + prefix + 'other_' + suffix).css('display', 'inline');
    $('#' + prefix + 'zone_select_' + suffix + ' , #' + prefix + 'choose_' + suffix).css('display', 'none');
    $('#' + prefix + 'zone_search_' + suffix).css('display', 'inline');
    $('#' + prefix + 'zone_find_' + suffix).css('display', 'none');
    $('#' + prefix + 'must_option_' + suffix).css('display', 'none'); 
}
function oncollapsesForm(suffix) {
    prefix = '';
    $('#' + prefix + 'update_' + suffix + ' , #' + prefix + 'delete_' + suffix).css('display', 'inline');
}


function collapseList(suffix) {
    collapsesList('', suffix);
}
function collapsesList(prefix, suffix) {
    $('.' + prefix + 'yvs_form_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_list_' + suffix).fadeIn(1000);
    $('.' + prefix + 'yvs_grid_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_att_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_tab_' + suffix).fadeOut(500);
    $('.' + prefix + 'conge_grid').css('display', 'block');
    $('.' + prefix + 'conge_list').css('display', 'none');
    $('.' + prefix + 'gridNav').css('display', 'block');
    $('.' + prefix + 'chek_line_' + suffix).attr('checked', false);
    $('#' + prefix + 'update_' + suffix + ', #' + prefix + 'delete_' + suffix + ' , #' + prefix + 'option_' + suffix).css('display', 'none');
    $('#' + prefix + 'save_' + suffix + ', #' + prefix + 'cancel_' + suffix + ', #' + prefix + 'other_' + suffix).css('display', 'none');
    $('#' + prefix + 'search_' + suffix + ', #' + prefix + 'find_' + suffix).css('display', 'inline');
    $('#' + prefix + 'zone_select_' + suffix + ' , #' + prefix + 'choose_' + suffix).css('display', 'none');
    $('#' + prefix + 'input_hide_' + suffix).val("");
    $('#' + prefix + 'zone_search_' + suffix).css('display', 'inline');
    $('#' + prefix + 'zone_find_' + suffix).css('display', 'none');
    $('#' + prefix + 'must_option_' + suffix).css('display', 'inline');

    return false;
}

function collapseGrid(suffix) {
    collapsesGrid('', suffix);
}

function collapsesGrid(prefix, suffix) {
    $('.' + prefix + 'yvs_form_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_list_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_grid_' + suffix).fadeIn(1000);
    $('.' + prefix + 'yvs_att_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_tab_' + suffix).fadeOut(500);
    $('.' + prefix + 'gridNav').css('display', 'block');
    $('.' + prefix + 'chek_line' + suffix).attr('checked', false);
    $('#' + prefix + 'update_' + suffix + ', #' + prefix + 'delete_' + suffix + ' , #' + prefix + 'option_' + suffix).css('display', 'none');
    $('#' + prefix + 'save_' + suffix + ', #' + prefix + 'cancel_' + suffix + ', #' + prefix + 'other_' + suffix).css('display', 'none');
    $('#' + prefix + 'search_' + suffix + ' , #' + prefix + 'find_' + suffix).css('display', 'none');
    $('#' + prefix + 'zone_select_' + suffix + ' , #' + prefix + 'choose_' + suffix).css('display', 'inline');
    $('#' + prefix + 'zone_search_' + suffix).css('display', 'none');
    $('#' + prefix + 'zone_find_' + suffix).css('display', 'inline');
    $('#' + prefix + 'must_option_' + suffix).css('display', 'inline');
    $('#' + prefix + 'input_hide_' + suffix).val("");
    return false;
}

function collapseTab(suffix) {
    collapsesTab('', suffix);
}
function collapsesTab(prefix, suffix) {
    $('.' + prefix + 'yvs_form_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_list_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_grid_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_att_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_tab_' + suffix).fadeIn(1000);
    $('.' + prefix + 'gridNav').css('display', 'block');
    $('.' + prefix + 'chek_line' + suffix).attr('checked', false);
    $('#' + prefix + 'update_' + suffix + ', #' + prefix + 'delete_' + suffix + ' , #' + prefix + 'option_' + suffix).css('display', 'none');
    $('#' + prefix + 'save_' + suffix + ', #' + prefix + 'cancel_' + suffix + ', #' + prefix + 'other_' + suffix).css('display', 'none');
    $('#' + prefix + 'zone_search_' + suffix + ' , #' + prefix + 'zone_find_' + suffix).css('display', 'none');
    $('#' + prefix + 'zone_select_' + suffix + ' , #' + prefix + 'choose_' + suffix).css('display', 'inline');
    $('#' + prefix + 'input_hide_' + suffix).val("");
    $('#' + prefix + 'must_option_' + suffix).css('display', 'none');
    return false;
}

function collapseAtt(suffix) {
    collapsesAtt('', suffix);
}
function collapsesAtt(prefix, suffix) {
    $('.' + prefix + 'yvs_form_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_list_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_grid_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_tab_' + suffix).fadeOut(500);
    $('.' + prefix + 'yvs_att_' + suffix).fadeIn(1000);
    $('.' + prefix + 'gridNav').css('display', 'block');
    $('.' + prefix + 'chek_line' + suffix).attr('checked', false);
    $('#' + prefix + 'update_' + suffix + ', #' + prefix + 'delete_' + suffix + ' , #' + prefix + 'option_' + suffix).css('display', 'none');
    $('#' + prefix + 'save_' + suffix + ', #' + prefix + 'cancel_' + suffix + ', #' + prefix + 'other_' + suffix).css('display', 'none');
    $('#' + prefix + 'search_' + suffix + ' , #' + prefix + 'find_' + suffix).css('display', 'none');
    $('#' + prefix + 'zone_select_' + suffix + ' , #' + prefix + 'choose_' + suffix).css('display', 'none');
    $('#' + prefix + 'input_hide_' + suffix).val("");
    return false;
}

function slideZoneSearche() {
    //$(".img_slide").css('display', 'none');
    $(".img_slide").click(function() {
        if (!$(".zone_find").hasClass("On")) {
            $(".zone_find").children().css('display', 'none');
            $(".zone_find").animate({
                bottom: '-100px'
            }, 400);
            $('body').find('.part_scroll').height($('body').find('.part_scroll').height() + 40);
            $(".zone_find").addClass("On");
            $(".img_slide").css('display', 'inline');
        } else {
            $(".zone_find").animate({
                bottom: '3.2%'
            }, 400);
            $('body').find('.part_scroll').height($('body').find('.part_scroll').height() - 40);
            $(".zone_find").children().css('display', 'block');
            $(".zone_find").removeClass("On");
            $(".img_slide").css('display', 'none');
        }
        $('.zone_find .txt_search').focus();
    });
}


function toogleFieldSet(node) {
    var legend = node;
    var value = node.children("span").html();
    if (value === "[-]")
        value = "[+]";
    else
        value = "[-]";
    node.siblings().slideToggle("fast", function() {
        legend.children("span").html(value);
    });
}

function slideShow(node) {
    slideShow(node, 200);
}

function slideShow(node, height) {
    var button = $('#' + node).find('.button_slide').html();
    if (button === "[-]") {
        slideShowAction(node, false, height);
    }
    else {
        slideShowAction(node, true, height);
    }
}

function slideShowAction(node, open, height) {
    height = height > 0 ? height : 200;
    if (!open) {
        closeShowAction(node, height);
    }
    else {
        openShowAction(node, height);
    }
}

function closeShowAction(node, height) {
    var button = $('#' + node).find('.button_slide').html();
    button = "[+]";
    $('#' + node).animate({
        height: '22px'
    }, 500);
    $('#' + node).find('.zone_slide').slideUp();
    $('#' + node).find('.zone_bottom').css('display', 'none');
    $('#' + node).find('.zone_title_infos').css('display', 'inline');
    $('#' + node).find('.button_slide').html(button);
}

function openShowAction(node, height) {
    var button = $('#' + node).find('.button_slide').html();
    button = "[-]";
    $('#' + node).animate({
        height: height + 'px'
    }, 500);
    $('#' + node).find('.zone_slide').slideDown();
    $('#' + node).find('.zone_bottom').css('display', 'inline');
    $('#' + node).find('.zone_title_infos').css('display', 'none');
    $('#' + node).find('.button_slide').html(button);
}

function slideOnClick(down, classe) {
//    var node = $(".display_cycle");
    var node = $("." + classe);
    var sdown = $(".ico_slide_down");
    var sup = $(".ico_slide_up");
    if (down) {
        node.slideDown();
        sdown.css("display", "none");
        sup.css("display", "inline");
    } else {
        node.slideUp();
        sdown.css("display", "block");
        sup.css("display", "none");
    }
}

function slideZoneOnClick(button, zone) {
    var node = $("#" + zone);
    var buton = $("." + button);
    var action = (button !== null) ? buton.html() : "[-]";
    if (action.trim() === "[+]") {
        node.slideDown();
        action = "[-]";
    } else {
        node.slideUp();
        action = "[+]";
    }
    if (button !== null) {
        buton.html(action);
    }
}
function slideZoneOnClick_(button, zone, change) {
    var node = $("#" + zone);
    var buton = $("." + button);
    var action = (button !== null) ? buton.html() : "[-]";
    if (action.trim() === "[+]") {
        node.slideDown();
        action = "[-]";
    } else {
        if (change) {
            node.slideUp();
            action = "[+]";
        }
    }
    if (button !== null) {
        buton.html(action);
    }
}

function openDialog(wdgt, noeud) {
    if (noeud.val() === '-1') {
        wdgt.show();
    }
}
function printBookmarks(id) {
//    console.log(chrome.bookmark);
}

/*--------------------------------------------------------*/
/*----------BEGIN RACCOURCIS------------------------*/
/*--------------------------------------------------------*/
$(function() {
    $('#left').attr('statut', 'hide');
    $('.svgMenu_l, .svgMenu_r').click(function() {
//        $('.left_content').addClass('left_open');
        var st = $('#left').attr('statut');
        if (st === 'hide') {
            $('.left_content').animate({
                'margin-left': '25%',
                'overflow-x': 'auto'
            }, 100);
            $('#left').animate({
                width: '24%'
            }, 50);
            $('#left').attr('statut', 'visible');
            $(".svgMenu_r").css('display', 'none');
            $(".svgMenu_l").css('display', 'inline');
        } else {
            $('.left_content').animate({
                'margin-left': '1.1%',
                width: '99%',
                opacity: 1
            }, 50);
            $('#left').animate({
                display: 'none',
                width: '0%'
            }, 100);
            $('#left').attr('statut', 'hide');
            $('.left_content').removeClass('left_open');
            $(".svgMenu_l").css('display', 'none');
            $(".svgMenu_r").css('display', 'inline');
        }
    });
    //click n'importe où dans la zone de traitements
    $('.left_content').click(function() {
        if ($('#left').attr('statut') !== 'hide') {
            $('#left').animate({
                display: 'none',
                width: '0%'
            }, 100);
            $('.left_content').animate({
                'margin-left': '1.1%',
                width: '99%',
                opacity: 1
            }, 500);
            $('#left').attr('statut', 'hide');
            $('.left_content').removeClass('left_open');
            $(".svgMenu_l").css('display', 'none');
            $(".svgMenu_r").css('display', 'inline');
        }
    });

    /*Les raccourcis claviers*/
    $(document).keydown(function(e) {
        var adresse = obtenirPropriete("origin");
        if (e.ctrlKey && e.keyCode === 70) {//ouvre la boite de dialogue article pour la combinaison ctrl+f            
            $(".img_slide").trigger('click');
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        if (e.ctrlKey && e.keyCode === 68) {//deconnection et fermeture de la session       ctrl + d
            $("#link_deconnect").trigger('click');
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        if (e.keyCode === 113) {//F2
            //ouvre les modules
            $('.svgMenu_l').trigger('click');
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        if (e.ctrlKey && e.keyCode === 97) {//ouvre la page d'accueil du module parametrage pour la combinaison ctrl+1            
            $(".mod_donne_de_base").trigger('click');
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        if (e.ctrlKey && e.keyCode === 98) {//ouvre la page d'accueil du module parametrage pour la combinaison ctrl+2    
            $(".mod_gescom").trigger('click');
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        if (e.ctrlKey && e.keyCode === 99) {//ouvre la page d'accueil du module parametrage pour la combinaison ctrl+3            
            $(".mod_gesProd").trigger('click');
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        if (e.ctrlKey && e.keyCode === 100) {//ouvre la page d'accueil du module compta pour la combinaison ctrl+4   
            $(".mod_gesCompta").trigger('click');
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        if (e.ctrlKey && e.keyCode === 101) {//ouvre la page d'accueil des ressources humaine pour la combinaison ctrl+5               
//            window.location = adresse + "/Lymytz_Web/Ressources Humaine";
            $(".moduleRH").trigger('click');
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        if (e.ctrlKey && e.keyCode === 103) {//ouvre la page d'accueil de la mutuelle pour la combinaison ctrl+7   
            $(".mod_gesmut").trigger('click');
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        if (e.ctrlKey && e.keyCode === 104) {//ouvre la page d'accueil du module parametrage pour la combinaison ctrl+8            
            $(".moduleParam").trigger('click');
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        if (e.ctrlKey && e.keyCode === 105) {//ouvre la page d'accueil du module statistique pour la combinaison ctrl+9
            $(".mod_Stat").trigger('click');
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
    });
});

function obtenirParametre(sVar) {
    return unescape(window.location.search.replace(new RegExp("^(?:.*[&\\?]" + escape(sVar).replace(/[\.\+\*]/g, "\\$&") + "(?:\\=([^&]*))?)?.*$", "i"), "$1"));
}

function obtenirPropriete(sVar) {
    var oLocation = window.location, aLog = ["Propriété (Typeof): valeur", "window.location (" + (typeof oLocation) + "): " + oLocation];
    for (var sProp in oLocation) {
        aLog.push(sProp + " (" + (typeof oLocation[sProp]) + "): " + (oLocation[sProp] || "n/a"));
        if (sVar === sProp) {
            return oLocation[sProp];
        }
    }
    return "";
}


function hideButtonDeleteQualification() {
    $('#delete_employeQualif').css('display', 'none');
}

/*Noicir la page*/

function pageDark() {
//    $('#page').css({'opacity':'0.1', background:'#000000'});
    $('#ajaxStatusPanel').addClass('modal');
}
function pageClair() {
    $('#page').css('opacity', '1');
    $('#ajaxStatusPanel').removeClass('modal');
}

/**Articles***/
function openDlgArt(source) {
    dlgArticleList.show();
    $("#hideChoixSourceArt").val(source);
}

function giveFocusTo() {
    $("#txt_zone_saisie_jour").focus();
    $("#txt_zone_saisie_jour").select();
}

function openDlgWait() {
    PF('dlgWait').show();
}
function closeDlgWait() {
    PF('dlgWait').hide();
}

/* 
 * -----------------
 * GESTION DE LA SELECTION DES TABLEAUX
 * ------------------
 */
var nbSelectionTab = 0;

function selectionAllLine2Tab(nbreLigne, suffix1, suffix2) {
    selectionAllLineTab(nbreLigne, suffix1);
    selectionAllLineTab(nbreLigne, suffix2);
}

function onselectLine(numLine, suffix) {
    var val = $('#input_hide_' + suffix).val();
    var tab = val.split('-');
    $(".chek_line_" + suffix).each(function() {
        if ($(this).is(':checked')) {
            $(this).trigger('click');
        }
    });

}
function onselectLine(suffix) {
    var val = $('#input_hide_' + suffix).val();
    var tab = val.split('-');
    $(".chek_line_" + suffix).each(function() {
        if ($(this).is(':checked')) {
            $(this).trigger('click');
        }
    });

}

function selectionAllLineTab(nbreLigne, suffix) {
    if ($('.chek_all_line_' + suffix).is(':checked')) {
        $(".chek_line_" + suffix).each(function() {
            if (!$(this).is(':checked')) {
                $(this).trigger('click');
            }
        });
    } else {
        var val = "";
        nbreLigne = 0;
        $('.chek_line_' + suffix).attr('checked', false);
        $('#input_hide_' + suffix).val("");
    }

    if (nbreLigne > 0) {
        $('.nbSelect' + suffix).text(nbreLigne + " Eléménts sélectioné");
    } else {
        $('.nbSelect' + suffix).text("");
    }
    setNbreSelectionTab(nbreLigne, suffix);
}

function selectionLine2Tab(numLine, suffix1, suffix2) {
    selectionLineTab(numLine, suffix1);
    selectionLineTab(numLine, suffix2);
}

function selectionLineTab(node, numLine, suffix) {
    selectionLineTab(numLine, suffix);
}

function selectionLineTab(numLine, suffix) {
    var val = $('#input_hide_' + suffix).val();
    var tab = val.split('-');
    var num = 0;
    var i = 0;
    var add = true;
    for (i = 0; i < tab.length; i++) {
        num = tab[i];
        if (parseInt(num) === numLine) {
            tab[i] = null;
            add = false;
            break;
        }
    }
    if (add) {
        val += numLine + "-";
    } else {
        numLine = "-" + numLine + "-";
        val = Remplace("-" + val, numLine, "-");
    }
    tab = val.split('-');
    val = "";
    nbSelectionTab = 0;
    for (i = 0; i < tab.length; i++) {
        num = tab[i];
        if (num !== "") {
            val += num + "-";
            nbSelectionTab++;
        }
    }
    $('#input_hide_' + suffix).val(val);
    if (nbSelectionTab > 0) {
        $('.nbSelect' + suffix).text(nbSelectionTab + " Elémént(s) sélectioné(s)");
    } else {
        $('.nbSelect' + suffix).text("");
    }
    setNbreSelectionTab(nbSelectionTab, suffix);
}
;

function selectionLineTab_(numLine, suffix) {
    $('.chek_line_' + suffix).eq(numLine).trigger('click');        //    
}

function setNbreSelectionTab(nbre_, suffix) {
    nbSelectionTab = parseInt(nbre_);
    selectionLineTable(suffix);
}

function selectionLineTable(suffix) {
    if (nbSelectionTab === 1) {
        $('#delete_' + suffix).css('display', 'inline');
        $('#option_' + suffix).css('display', 'inline');
        $('#update_' + suffix).css('display', 'inline');
        $('#other_' + suffix).css('display', 'inline');
    } else {
        $('#delete_' + suffix).css('display', 'none');
        $('#option_' + suffix).css('display', 'none');
        $('#update_' + suffix).css('display', 'none');
        $('#other_' + suffix).css('display', 'none');
    }
    if (nbSelectionTab > 1) {
        $('#delete_' + suffix).css('display', 'inline');
        $('#option_' + suffix).css('display', 'inline');
        $('#update_' + suffix).css('display', 'none');
        $('#other_' + suffix).css('display', 'inline');
    }
}

function Remplace(expr, a, b) {
    var i = 0;
    while (i !== -1) {
        i = expr.indexOf(a, i);
        if (i >= 0) {
            expr = expr.substring(0, i) + b + expr.substring(i + a.length);
            i += b.length;
        }
    }
    return expr;
}

/* 
 * -------------------------
 * aUTRES FONCTIONS DE GESTION DES FORMULAIRES
 * -------------------------------
 */
function valueChange(suffix, noeud) {
    if (noeud.val().length > 0) {
        $('#cancel_' + suffix).find('.ui-button-text').html('Annuler');
    } else {
        $('#cancel_' + suffix).find('.ui-button-text').html('Nouveau');
    }
}

function initRegle_() {
    $('.signes_op').css('cursor', 'pointer');
}


/******************************BULLETINS DE PAYE*****************************/

function choixHeader_() {
    $("#refAction").val('N');     //indique  qu'on veux créer un en-tête normale pour les bulletins

}

function choixHeader() {
    dlgOrdreCalcul.show();
    $("#refAction").val('P');   //indique  qu'on veux planifier l'ordre
}


/*******************CONTRAT******************************/
function opendlgAddPreavis(value, i1, i2) {
    dlgAddpreavis.show();
    $("#inputValIntervale").val(value);
    $("#inputValCintervale").val(value);
    $(".ii1").text(i1);
    $(".ii2").text(i2);
}


/*******************PRESENCE******************************/

function openDlgValidePointage(noeud) {
    if (noeud.is(':checked')) {
        dlgConfirmValid.show();
    } else {
        dlgConfirmInValid.show();
    }
}
var displayMarge = false;
function displayChampUpdateMarge() {
    if (displayMarge) {
        $(".displayMarge").show();
        displayMarge = false;
    } else {
        $(".displayMarge").hide();
        displayMarge = true;
    }
}
var displayHs = false;
function displayChampUpdateHs() {
    if (displayHs) {
        $(".displayHs").show();
        displayHs = false;
    } else {
        $(".displayHs").hide();
        displayHs = true;
    }
}

function hideHistorique() {
    $('.historique_presence').css('display', 'none');
}
function showHistorique() {
    $('.historique_presence').css('display', 'block');
    collapseForm('presenceEmps');
    $('.yvs_form_presenceEmps').css('display', 'none');
}

/********************Planing de travail********************/
function changeViewPlaningWork() {
    var str = $('.tabPlaning').attr('display');
    if (str === 'ON') {
        $('.tabPlaning').css('display', 'block');
        $('.gridPlaning').css('display', 'none');
        $('.tabPlaning').attr('display', 'OFF');
    } else {
        $('.tabPlaning').css('display', 'none');
        $('.gridPlaning').css('display', 'block');
        $('.tabPlaning').attr('display', 'ON');
    }
}


/*Formulaire de gestion des congés*/

function choixNatureAbscence() {
    var v = $('input[type=radio][name=selectNaturePerm]:checked').attr('value');
    if (v === 'P') {
        $('.conge_champDuree').css('display', 'inline');
        $('.conge_typeConge').css('display', 'none');
        if ($('input[type=radio][name=selectTypeDuree]:checked').attr('value') === 'C') {
            $('.conge_label_heure, .conge_endDate, .conge_label_heure_').css('display', 'none');
            $('.conge_labelDuree').text('Durée (en heure)');
        }
    } else {
        $('.conge_label_heure, .conge_startDate, .conge_endDate, .conge_label_heure_').css('display', 'inline');
        $('.conge_champDuree').css('display', 'none');
        $('.conge_typeConge').css('display', 'inline');
    }
}

function choixDureeAbscence() {
    var v = $('input[type=radio][name=selectTypeDuree]:checked').attr('value');
    if (v === 'C') {
        //si c'est une permission à courte durée, on cache les champs dates
        $('.conge_label_dateDebut').text('Heure de début');
        $('.conge_label_dateFin').text('Heure de Fin');
        $('.conge_label_heure, .conge_endDate, .conge_label_heure_').css('display', 'none');
        //modifie le label de la durée
        $('.conge_labelDuree').text('Durée (en heure)');

    } else {
        $('.conge_label_dateDebut').text('Date de début');
        $('.conge_label_dateFin').text('Date de Fin');
        $('.conge_label_heure, .conge_startDate, .conge_endDate, .conge_label_heure_').css('display', 'inline');
        //modifie le label de la durée
        $('.conge_labelDuree').text('Durée (en jour)');
        $("#txt_date_paiement_conge").css('display', 'none');
    }
}

function conge_giveFocus() {
    $("#chp_descriptionCOnge").focus();
    choixDureeAbscence();
}


/*Vue contrat*/

//choix du la source de la date d'evaluation du premier congé
function choixParamFirstConge(node) {
    var val = node.val();
    if (val === 'DF') {
        $(".paramDateF").css('display', 'inline');
    } else {
        $(".paramDateF").css('display', 'none');
    }
}

/***Slide de la zone des ordres de fabrication***/

function slideLaterale() {
    if (!$(".zone_of_left").hasClass("hide")) {
        $(".zone_of_left").animate({
            width: '0%'
        }, 400);
        $(".zone_of_rigth").animate({
            'margin-left': '1%'
        }, 400);
        $(".main_relais").css('display', 'block');
        $(".zone_of_left").addClass("hide");
    } else {
        $(".zone_of_left").animate({
            width: '40%'
        }, 400);
        $(".zone_of_rigth").animate({
            'margin-left': '41%'
        }, 400);
        $(".zone_of_left").removeClass("hide");
        $(".main_relais").css('display', 'none');
    }
}