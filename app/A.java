<!-- Gender -->
                <com.google.android.material.textfield.TextInputLayout
                    style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="12dp"
                    android:hint="Gender">
                    <com.google.android.material.textfield.TextInputEditText
                        android:id="@+id/etGender"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"/>
                </com.google.android.material.textfield.TextInputLayout>

                <!-- Divider -->
                <View
                    android:layout_width="match_parent"
                    android:layout_height="1dp"
                    android:layout_marginVertical="20dp"
                    android:background="@color/divider"/>

                <!-- Contact Info Section -->
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Contact Info"
                    android:textStyle="bold"
                    android:textSize="16sp"
                    android:textColor="@color/textPrimary"/>

                <!-- Phone Number -->
                <com.google.android.material.textfield.TextInputLayout
                    style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="8dp"
                    android:hint="Phone Number">
                    <com.google.android.material.textfield.TextInputEditText
                        android:id="@+id/etPhone"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:inputType="phone"/>
                </com.google.android.material.textfield.TextInputLayout>

                <!-- Email -->
                <com.google.android.material.textfield.TextInputLayout
                    style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="12dp"
                    android:hint="Email">
                    <com.google.android.material.textfield.TextInputEditText
                        android:id="@+id/etEmail"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:inputType="textEmailAddress"/>
                </com.google.android.material.textfield.TextInputLayout>

            </LinearLayout>
        </ScrollView>
    </com.google.android.material.card.MaterialCardView>

    <!-- Save button pinned at bottom -->
    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnSave"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:text="Save"
        app:cornerRadius="8dp"
        app:backgroundTint="@color/primaryColor"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>