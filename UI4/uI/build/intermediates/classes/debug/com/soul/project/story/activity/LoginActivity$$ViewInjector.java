// Generated code from Butter Knife. Do not modify!
package com.soul.project.story.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class LoginActivity$$ViewInjector<T extends com.soul.project.story.activity.LoginActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492965, "field 'layoutRoot'");
    target.layoutRoot = finder.castView(view, 2131492965, "field 'layoutRoot'");
  }

  @Override public void reset(T target) {
    target.layoutRoot = null;
  }
}
