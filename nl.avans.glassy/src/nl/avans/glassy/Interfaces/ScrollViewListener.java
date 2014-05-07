package nl.avans.glassy.Interfaces;

import nl.avans.glassy.Controllers.ObservableScrollView;

public interface ScrollViewListener {

    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

}