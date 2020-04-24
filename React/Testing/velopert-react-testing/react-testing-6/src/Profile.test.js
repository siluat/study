import React from "react";
import { render } from "@testing-library/react";
import Profile from "./Profile";

describe("<Profile />", () => {
  it("matches snapshot", () => {
    const utils = render(<Profile username="siluat" name="siluat" />);
    expect(utils.container).toMatchSnapshot();
  });
  it("shows the props correctly", () => {
    const utils = render(<Profile username="siluat" name="siluat" />);
    utils.getByText("siluat");
    utils.getByText("(siluat)");
    utils.getByText(/\(sil/);
  });
});
