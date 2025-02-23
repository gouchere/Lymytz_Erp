/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var nbSelectionTab = 0;

function selectionAllLine2Tab(nbreLigne, suffix1, suffix2) {
    selectionAllLineTab(nbreLigne, suffix1);
    selectionAllLineTab(nbreLigne, suffix2);
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
