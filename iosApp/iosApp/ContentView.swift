//
//  ContentView.swift
//  iosApp
//
//  Created by Auriane POIRIER on 12/12/2024.
//

import SwiftUI
import shared

struct ContentView: View {
    private let loginViewModelFactory: LoginViewModelFactory

    init() {
        let repository = LoginRepository()
        self.loginViewModelFactory = LoginViewModelFactory(repository: repository)
    }

    var body: some View {
        LoginPage(viewModelFactory: loginViewModelFactory)
    }
}

#Preview {
    ContentView()
}

