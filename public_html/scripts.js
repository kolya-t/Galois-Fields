function change(checkbox) {
        // var el = evt.target;
        var val = checkbox.value|0; // to int

        if (checkbox.checked) {
            form.table_first.disabled = false;
            form.table_count.disabled = false;
        } else {
            form.table_first.disabled = true;
            form.table_count.disabled = true;
        }
    };
