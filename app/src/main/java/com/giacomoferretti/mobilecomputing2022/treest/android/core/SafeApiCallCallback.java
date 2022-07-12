package com.giacomoferretti.mobilecomputing2022.treest.android.core;

import java.io.IOException;

public interface SafeApiCallCallback<T> {
    Result<T> call() throws IOException;
}
