package com.wenhua.wenhua.controllers.base;

import com.shizhefei.fragment.LazyFragment;

/**
 * Created by castiel on 2016/8/4.
 */

public abstract class BaseFragment extends LazyFragment {

    public abstract void initView();
    public abstract void initListener();
}
