package com.testfxlab;

import com.testfxlab.model.Human;

public interface MainApi {

    void delete(int humanId);

    void create(Human newHuman);

    void update(Human updatedHuman);

    Human showEditDialog(Human selected);
}
