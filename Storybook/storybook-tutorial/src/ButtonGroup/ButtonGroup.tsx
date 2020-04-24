/** @jsx jsx */
import { css, jsx } from "@emotion/core";

export type ButtonGroupProps = {
  /** 버튼을 보여줄 방향 */
  direction: "row" | "column";
  /** 버튼을 우측에 보여줍니다. */
  rightAlign?: boolean;
  /** 버튼과 버튼사이의 간격을 설정합니다. */
  gap: number | string;
  /** 버튼 그룹에서 보여줄 버튼들 */
  children: React.ReactNode;
  className?: string;
};

const ButtonGroup = ({
  direction,
  rightAlign,
  gap,
  children,
  className,
}: ButtonGroupProps) => {
  return (
    <div
      css={[
        { display: "flex", flexDirection: direction },
        gapStyle(direction, gap),
        rightAlign && rightAlignStyle,
      ]}
      className={className}
    >
      {children}
    </div>
  );
};

ButtonGroup.defaultProps = {
  direction: "row",
  gap: "0.5rem",
};

const gapStyle = (direction: "row" | "column", gap: number | string) => {
  const marginType = direction === "row" ? "marginLeft" : "marginTop";
  return css({
    "button + button": {
      [marginType]: gap,
    },
  });
};

const rightAlignStyle = css`
  justify-content: flex-end;
`;

export default ButtonGroup;
